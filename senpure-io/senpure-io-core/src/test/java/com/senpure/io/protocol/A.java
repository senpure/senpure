package com.senpure.io.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;

public class A {

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
                    System.out.println("Discard upper 32 bits 1  ");
                    result |= (tmp & 0x7f) << 21;
                    result |= (tmp = buf.readByte()) << 28;
                    if (tmp < 0) {
                        System.out.println("Discard upper 32 bits 2 ");
                        throw new RuntimeException();
                        // Discard upper 32 bits.
//                        for (int i = 0; i < 5; i++) {
//                            if (buf.readByte() >= 0) {
//                                return result;
//                            }
//                        }
                    }
                }
            }
        }
        return result;
    }

    private  static  String formatByteBuf( ByteBuf msg) {
        String eventName = "ppp";
        String chStr = "8";
        int length = msg.readableBytes();
        if (length == 0) {
            StringBuilder buf = new StringBuilder(chStr.length() + 1 + eventName.length() + 4);
            buf.append(chStr).append(' ').append(eventName).append(": 0B");
            return buf.toString();
        } else {
            int outputLength = chStr.length() + 1 + eventName.length() + 2 + 10 + 1;

                int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
                int hexDumpLength = 2 + rows * 80;
                outputLength += hexDumpLength;


            StringBuilder buf = new StringBuilder(outputLength);
            buf.append(chStr).append(' ').append(eventName).append(": ").append(length).append('B');

                buf.append(StringUtil.NEWLINE);
                ByteBufUtil.appendPrettyHexDump(buf, msg);



            return buf.toString();
        }
    }
    public static void writeVar32(ByteBuf buf, int value) {
        //16进制   0x7F      0x80
        //十进制   127       128
        //二进制   01111111  10000000
        while (true) {
            if ((value & ~0x7F) == 0) {
                System.out.println("write "+value+" "+Integer.toBinaryString(value)+" "+Integer.toHexString(value));
                buf.writeByte(value);
                return;
            } else {
                int temp=(value & 0x7F) | 0x80;
                buf.writeByte(temp);
                System.out.println("write "+temp+" "+Integer.toBinaryString(temp)+" "+Integer.toHexString(temp));
                value >>>= 7;
            }
        }
    }
    public static void showVar32(int value) {
        System.out.println("number:" + value);
        String binary= Integer.toBinaryString(value);
        System.out.println("binary:" +binary);
        StringBuilder sb = new StringBuilder();

        int size = binary.length();
        for (int i = size-1,j=0; i >=0 ; i--,j++) {
            if(j>0 &&j % 7 == 0)
            {
                sb.insert(0, ",");
            }
            sb.insert(0, binary.charAt(i));

        }

        System.out.println("group: "+sb.toString());
        ByteBuf buf = Unpooled.buffer(5);
         writeVar32(buf, value);


        while (buf.isReadable()) {
            byte b = buf.readByte();
            //b = (byte) (b|0x7f);
           // System.out.println(b);
            sb.append(Integer.toBinaryString(b)).append(",");
        }
       // sb.append("\n");
    //  ByteBufUtil.appendPrettyHexDump(sb, buf);
       // System.out.println(sb.toString());


        System.out.println(CompressBean.computeVar32Size(value));

    }

    private  static  void binary(String binary)
    {
        System.out.println(Integer.parseInt(binary,2));

    }
    public static void main(String[] args) {



       showVar32(666666);


    }

}
