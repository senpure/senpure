package com.senpure.io.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * CompressBean
 *
 * @author senpure
 * @time 2020-01-06 10:58:47
 */
public abstract class CompressBean implements Bean {

    public static final int WIRE_TYPE_VARINT = 0;
    public static final int WIRE_TYPE_FIXED32 = 1;
    public static final int WIRE_TYPE_FIXED64 = 2;
    public static final int WIRE_TYPE_LENGTH_DELIMITED = 3;

    public static int encodeZigZag32(int value) {
        return value << 1 ^ value >> 31;
    }

    public static long encodeZigZag64(long value) {
        return value << 1 ^ value >> 63;
    }

    public static void writeVar32(ByteBuf buf, int value) {
        //16进制   0x7F      0x80
        //十进制   127       128
        //二进制   01111111  10000000
        while (true) {
            if ((value & ~0x7F) == 0) {
                buf.writeByte(value);
                return;
            } else {
                buf.writeByte((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    public static void writeVar64(ByteBuf buf, long value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                buf.writeByte((int) value);
                return;
            } else {
                buf.writeByte(((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }


    public static void writeVar32(ByteBuf buf, int tag, int value) {
        //Constant.WIRETYPE_VARINT
        writeVar32(buf, tag);
        writeVar32(buf, value);
    }

    public static void writeVar64(ByteBuf buf, int tag, long value) {
        writeVar32(buf, tag);
        writeVar64(buf, value);
    }

    public static void writeBoolean(ByteBuf buf, int tag, boolean value) {
        writeVar32(buf, tag);
        writeBoolean(buf, value);
    }

    public static void writeBoolean(ByteBuf buf, boolean value) {
        buf.writeByte(value ? 1 : 0);
    }

    public static void writeSint(ByteBuf buf, int tag, int value) {
        writeVar32(buf, tag);
        writeVar32(buf, encodeZigZag32(value));
    }

    public static void writeSint(ByteBuf buf, int value) {
        writeVar32(buf, encodeZigZag32(value));
    }


    public static void writeSlong(ByteBuf buf, int tag, long value) {
        writeVar32(buf, tag);
        writeVar64(buf, encodeZigZag64(value));
    }

    public static void writeSlong(ByteBuf buf, long value) {
        writeVar64(buf, encodeZigZag64(value));
    }


    public static void writeFixed32(ByteBuf buf, int tag, int value) {
        writeVar32(buf, tag);
        buf.writeInt(value);
    }

    public static void writeFixed32(ByteBuf buf, int value) {
        buf.writeInt(value);
    }


    public static void writeFixed64(ByteBuf buf, int tag, long value) {
        writeVar32(buf, tag);
        buf.writeLong(value);
    }

    public static void writeFixed64(ByteBuf buf, long value) {

        buf.writeLong(value);
    }

    public static void writeFloat(ByteBuf buf, int tag, float value) {

        writeVar32(buf, tag);
        buf.writeFloat(value);
    }

    public static void writeFloat(ByteBuf buf, float value) {
        buf.writeFloat(value);
    }

    public static void writeDouble(ByteBuf buf, int tag, double value) {
        writeVar32(buf, tag);
        buf.writeDouble(value);
    }

    public static void writeDouble(ByteBuf buf, double value) {
        buf.writeDouble(value);
    }


    public static void writeString(ByteBuf buf, int tag, String value) {
        writeVar32(buf, tag);
        writeString(buf, value);
    }

    public static void writeString(ByteBuf buf, String value) {
        writeString(buf, value, StandardCharsets.UTF_8);
    }

    public static void writeString(ByteBuf buf, String value, Charset charset) {
        byte[] bytes = value.getBytes(charset);
        writeVar32(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    public static void writeBytes(ByteBuf buf, int tag, byte[] value) {
        writeVar32(buf, tag);
        writeBytes(buf, value);
    }

    public static void writeBytes(ByteBuf buf, byte[] value) {
        writeVar32(buf, value.length);
        buf.writeBytes(value);
    }

    public static void writeBean(ByteBuf buf, int tag, Bean value) {
        if (value != null) {
            writeVar32(buf, tag);
            writeVar32(buf, value.serializedSize());
            value.write(buf);
        }
    }


    //read ↓↓↓↓↓↓↓

    public static int decodeZigZag32(int value) {
        return value >>> 1 ^ -(value & 1);
    }

    public static long decodeZigZag64(long value) {
        return value >>> 1 ^ -(value & 1L);
    }

    public static int tryReadVar32(ByteBuf buf) {
        if (!buf.isReadable()) {
            return 0;
        }
        byte tmp = buf.readByte();
        if (tmp >= 0) {
            return tmp;
        } else {
            int result = tmp & 127;
            if (!buf.isReadable()) {
                buf.resetReaderIndex();
                return 0;
            }
            if ((tmp = buf.readByte()) >= 0) {
                result |= tmp << 7;
            } else {
                result |= (tmp & 127) << 7;
                if (!buf.isReadable()) {
                    buf.resetReaderIndex();
                    return 0;
                }
                if ((tmp = buf.readByte()) >= 0) {
                    result |= tmp << 14;
                } else {
                    result |= (tmp & 127) << 14;
                    if (!buf.isReadable()) {
                        buf.resetReaderIndex();
                        return 0;
                    }
                    if ((tmp = buf.readByte()) >= 0) {
                        result |= tmp << 21;
                    } else {
                        result |= (tmp & 127) << 21;
                        if (!buf.isReadable()) {
                            buf.resetReaderIndex();
                            return 0;
                        }

                        result |= (tmp = buf.readByte()) << 28;
                        if (tmp < 0) {
                            // Discard upper 32 bits.
                            for (int i = 0; i < 5; i++) {
                                if (buf.readByte() >= 0) {
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }
    }

    public static int readVar32(ByteBuf buf) {
        byte tmp = buf.readByte();
        if (tmp >= 0) {
            return tmp;
        }
        int result = tmp & 0x7f;
        if ((tmp = buf.readByte()) >= 0) {
            result |= tmp << 7;
        } else {
            result |= (tmp & 0x7f) << 7;
            if ((tmp = buf.readByte()) >= 0) {
                result |= tmp << 14;
            } else {
                result |= (tmp & 0x7f) << 14;
                if ((tmp = buf.readByte()) >= 0) {
                    result |= tmp << 21;
                } else {
                    result |= (tmp & 0x7f) << 21;
                    result |= (tmp = buf.readByte()) << 28;
                    if (tmp < 0) {
                        // Discard upper 32 bits.
                        for (int i = 0; i < 5; i++) {
                            if (buf.readByte() >= 0) {
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static long readVar64(ByteBuf buf) {
        int shift = 0;
        long result = 0;
        while (shift < 64) {
            final byte b = buf.readByte();
            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
            shift += 7;
        }
        return result;
    }

    public static String readString(ByteBuf buf) {
        return readString(buf, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buf, Charset charset) {
        byte[] bytes = new byte[readVar32(buf)];
        buf.readBytes(bytes);
        return new String(bytes, charset);
    }

    public static byte[] readBytes(ByteBuf buf) {
        byte[] bytes = new byte[readVar32(buf)];
        buf.readBytes(bytes);
        return bytes;
    }

    public static boolean readBoolean(ByteBuf buf) {
        return buf.readBoolean();
    }

    public static int readSint(ByteBuf buf) {

        return decodeZigZag32(readVar32(buf));
    }

    public static long readSlong(ByteBuf buf) {

        return decodeZigZag64(readVar64(buf));
    }

    public static int readFixed32(ByteBuf buf) {

        return buf.readInt();
    }

    public static long readFixed64(ByteBuf buf) {

        return buf.readLong();
    }

    public static float readFloat(ByteBuf buf) {

        return buf.readFloat();
    }

    public static double readDouble(ByteBuf buf) {

        return buf.readDouble();
    }

    public static void readBean(ByteBuf buf, Bean value) {
        value.read(buf, readVar32(buf) + buf.readerIndex());

    }


    static int getTagWireType(int tag) {
        return tag & 7;
    }

    public static int getTagFieldNumber(int tag) {
        return tag >>> 3;
    }

    public static int makeTag(int fieldIndex, int writeType) {
        return fieldIndex << 3 | writeType;
    }

    public static int readTag(ByteBuf buf, int endIndex) {
        if (buf.readerIndex() == endIndex) {
            return 0;
        }
        return readVar32(buf);
    }

    public void skip(ByteBuf buf, int tag) {
        switch (getTagWireType(tag)) {
            case WIRE_TYPE_VARINT:
                readVar64(buf);
                break;
            case WIRE_TYPE_FIXED32:
                buf.skipBytes(4);
                break;
            case  WIRE_TYPE_FIXED64:
                buf.skipBytes(8);
                break;
            case WIRE_TYPE_LENGTH_DELIMITED:
                buf.skipBytes(readVar32(buf));
                break;
        }
    }

    public static <T> int computeEnumSize(List<T> values, Function<T, Integer> function) {
        int size = 0;
        for (T value : values) {
            size += function.apply(value);
        }
        return size;
    }


    public static int computePackedSize(int tagVar32Size, int serializedSize) {
        if (serializedSize > 0) {
            tagVar32Size += serializedSize;
            tagVar32Size += computeVar32Size(serializedSize);
            return tagVar32Size;
        }
        return 0;
    }

    public static int computeStringSize(int tagVar32Size, String value) {
        return tagVar32Size + computeStringSize(value);
    }

    public static int computeStringSize(int tagVar32Size, String value, Charset charset) {
        return tagVar32Size + computeStringSize(value, charset);
    }

    public static int computeStringSize(String value) {

        return computeStringSize(value, StandardCharsets.UTF_8);
    }

    public static int computeStringSize(String value, Charset charset) {
        byte[] bytes = value.getBytes(charset);
        return _computeVar32Size(bytes.length) + bytes.length;
    }

    public static int computeStringSize(int tagVar32Size, List<String> values) {
        int size = 0;
        for (String value : values) {
            size += computeStringSize(tagVar32Size, value);
        }
        return size;
    }

    public static int computeStringSize(int tagVar32Size, List<String> values, Charset charset) {
        int size = 0;
        for (String value : values) {
            size += computeStringSize(tagVar32Size, value, charset);
        }
        return size;
    }

    public static int computeBytesSize(int tagVar32Size, byte[] value) {
        return tagVar32Size + computeBytesSize(value);
    }

    public static int computeBytesSize(byte[] value) {
        return _computeVar32Size(value.length) + value.length;
    }

    public static int computeBeanSize(int tagVar32Size, Bean value) {
        return tagVar32Size + computeBeanSize(value);
    }

    public static int computeBeanSize(int tagVar32Size, List<Bean> values) {
        int size = 0;
        for (Bean value : values) {
            size += computeBeanSize(tagVar32Size, value);
        }
        return size;
    }

    public static int computeBeanSize(Bean value) {
        int size = value.serializedSize();
        return _computeVar32Size(size) + size;
    }

    public static int computeBooleanSize(int tagVar32Size, boolean value) {
        return tagVar32Size + computeBooleanSize(value);
    }

    public static int computeBooleanSize(List<Boolean> values) {
        return values.size();
    }

    public static int computeBooleanSize(boolean value) {
        return 1;
    }

    public static int computeDoubleSize(int tagVar32Size, double value) {
        return tagVar32Size + computeDoubleSize(value);
    }

    public static int computeDoubleSize(List<Double> values) {
        return values.size() << 3;
    }

    public static int computeDoubleSize(double value) {
        return 8;
    }

    public static int computeFloatSize(int tagVar32Size, float value) {
        return tagVar32Size + computeFloatSize(value);
    }

    public static int computeFloatSize(List<Float> values) {
        return values.size() << 2;
    }

    public static int computeFloatSize(float value) {
        return 4;
    }

    public static int computeFixed32Size(int tagVar32Size, int value) {
        return tagVar32Size + computeFixed32Size(value);
    }

    public static int computeFixed32Size(List<Integer> values) {
        return values.size() << 2;
    }

    public static int computeFixed32Size(int value) {
        return 4;
    }

    public static int computeFixed64Size(int tagVar32Size, long value) {
        return tagVar32Size + computeFixed64Size(value);
    }

    public static int computeFixed64Size(List<Long> values) {
        return values.size() << 3;
    }

    public static int computeFixed64Size(long value) {
        return 8;
    }

    public static int computeSintSize(int tagVar32Size, int value) {
        return tagVar32Size + computeSintSize(value);
    }

    public static int computeSintSize(List<Integer> values) {
        int size = 0;
        for (Integer value : values) {
            size += computeSintSize(value);
        }
        return size;
    }

    public static int computeSintSize(int value) {
        return _computeVar32Size(encodeZigZag32(value));
    }

    public static int computeSlongSize(int tagVar32Size, long value) {
        return tagVar32Size + computeSlongSize(value);
    }

    public static int computeSlongSize(List<Long> values) {
        int size = 0;
        for (Long value : values) {
            size += computeSlongSize(value);
        }
        return size;
    }

    public static int computeSlongSize(long value) {
        return _computeVar64Size(encodeZigZag64(value));
    }


    public static int computeVar32Size(int tagVar32Size, int value) {
        return tagVar32Size + computeVar32Size(value);

    }

    public static int computeVar32Size(List<Integer> values) {

        int size = 0;
        for (Integer value : values) {
            size += computeVar32Size(value);
        }
        return size;


    }

    public static int computeVar32Size(int value) {
        return value >= 0 ? _computeVar32Size(value) : 5;
    }

    public static int computeVar64Size(int tagVar32Size, long value) {
        return tagVar32Size + computeVar64Size(value);

    }

    public static int computeVar64Size(List<Long> values) {
        int size = 0;
        for (Long value : values) {
            size += computeVar64Size(value);
        }
        return size;


    }

    private static int _computeVar32Size(int value) {
        if ((value & -128) == 0) {
            return 1;
        } else if ((value & -16384) == 0) {
            return 2;
        } else if ((value & -2097152) == 0) {
            return 3;
        } else {
            return (value & -268435456) == 0 ? 4 : 5;
        }
    }

    public static int computeVar64Size(long value) {
        return value >= 0 ? _computeVar64Size(value) : 10;
    }

    private static int _computeVar64Size(long value) {
        if ((value & -128L) == 0L) {
            return 1;
        } else if ((value & -16384L) == 0L) {
            return 2;
        } else if ((value & -2097152L) == 0L) {
            return 3;
        } else if ((value & -268435456L) == 0L) {
            return 4;
        } else if ((value & -34359738368L) == 0L) {
            return 5;
        } else if ((value & -4398046511104L) == 0L) {
            return 6;
        } else if ((value & -562949953421312L) == 0L) {
            return 7;
        } else if ((value & -72057594037927936L) == 0L) {
            return 8;
        } else {
            return (value & -9223372036854775808L) == 0L ? 9 : 10;
        }
    }


    public static void append(StringBuilder sb, Bean value, String indent, String nextIndent) {
        if (value != null) {
            sb.append(value.toString(indent + nextIndent));
        } else {
            sb.append("null");
        }
    }

    public static void append(StringBuilder sb, Enum value) {
        if (value != null) {
            sb.append(value);
        } else {
            sb.append("null");
        }
    }

    public static void append(StringBuilder sb, Enum value, Function<Enum, String> function) {
        if (value != null) {
            sb.append(function.apply(value));
        } else {
            sb.append("null");
        }
    }

    public static <T> void appendValues(StringBuilder sb, List<T> values, String indent, String nextIndent) {
        if (values.size() > 0) {
            sb.append("[");
            for (T value : values) {
                sb.append("\n");
                sb.append(nextIndent);
                sb.append(indent).append(value);
            }
            sb.append("\n");
            sb.append(nextIndent);
            sb.append(indent).append("]");
        } else {
            sb.append("[]");
        }
    }

    public static <T extends Bean> void appendBeans(StringBuilder sb, List<T> beans, String indent, String nextIndent) {
        if (beans.size() > 0) {
            sb.append("[");
            for (Bean value : beans) {
                sb.append("\n");
                sb.append(nextIndent);
                sb.append(indent);
                sb.append(value.toString(indent + nextIndent));
            }
            sb.append("\n");
            sb.append(nextIndent);
            sb.append(indent).append("]");
        } else {
            sb.append("[]");
        }
    }

    public static void copyBytes(List<byte[]> source, List<byte[]> dest) {
        for (byte[] bytes : source) {
            byte[] copy = new byte[bytes.length];
            System.arraycopy(bytes, 0, copy, 0, bytes.length);
            dest.add(copy);
        }
    }

    public static byte[] copyBytes(byte[] source) {
        if (source == null) {
            return null;
        }
        byte[] copy = new byte[source.length];
        System.arraycopy(source, 0, copy, 0, source.length);
        return copy;
    }

    public static String bytesToString(byte[] value) {
        return Arrays.toString(value);
    }
}
