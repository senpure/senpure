package com.senpure.io.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * BeanTest
 *
 * @author senpure
 * @time 2019-09-17 10:13:39
 */
public class CompressBeanTest {

    // public static int TWO_TO_31 = 2147483648;


    @BeforeAll
    public static void before() {
        Unpooled.buffer(50);
    }

    @Test
    public void var32Test() {
        
        ByteBuf buf = Unpooled.buffer(500);
        for (int i = 0; i < 31; i++) {
            int value = 1 << i;
            int write = -value;
            System.out.println("write var32:" + write+" size:"+CompressBean.computeVar32Size(write));
            CompressBean.writeVar32(buf, write);
            int read = CompressBean.readVar32(buf);
            System.out.println("read var32:" + read+" size:"+CompressBean.computeVar32Size(read));
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write var32:" + write+" size:"+CompressBean.computeVar32Size(write));
            CompressBean.writeVar32(buf, write);
            read = CompressBean.readVar32(buf);
            System.out.println("read var32:" + read+" size:"+CompressBean.computeVar32Size(read));
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        int write = Integer.MIN_VALUE;
        System.out.println("write var32:" + write+" size:"+CompressBean.computeVar32Size(write));
        CompressBean.writeVar32(buf, write);
        int read = CompressBean.readVar32(buf);
        System.out.println("read var32:" + read+" size:"+CompressBean.computeVar32Size(read));
       Assertions.assertEquals(write, read);
        write = Integer.MAX_VALUE - 1;
        System.out.println("write var32:" + write+" size:"+CompressBean.computeVar32Size(write));
        CompressBean.writeVar32(buf, write);
        read = CompressBean.readVar32(buf);
        System.out.println("read var32:" + read+" size:"+CompressBean.computeVar32Size(read));
       Assertions.assertEquals(write, read);

    }

    @Test
    public void var64Test() {
        ByteBuf buf = Unpooled.buffer(1000);
        for (long i = 0; i < 63; i++) {
            long value = 1L << i;
            long write = -value;
            System.out.println("write var64:" + write);
            CompressBean.writeVar64(buf, write);
            long read = CompressBean.readVar64(buf);
            System.out.println("read var64:" + read);
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write var64:" + write);
            CompressBean.writeVar64(buf, write);
            read = CompressBean.readVar64(buf);
            System.out.println("read var64:" + read);
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        long write = Long.MIN_VALUE;
        System.out.println("write var64:" + write);
        CompressBean.writeVar64(buf, write);
        long read = CompressBean.readVar64(buf);
        System.out.println("read var64:" + read);
       Assertions.assertEquals(write, read);
        write = Long.MAX_VALUE;
        System.out.println("write var64:" + write);
        CompressBean.writeVar64(buf, write);
        read = CompressBean.readVar64(buf);
        System.out.println("read var64:" + read);
       Assertions.assertEquals(write, read);
    }


    @Test
    public void sintTest() {
        ByteBuf buf = Unpooled.buffer(500);
        for (int i = 0; i < 31; i++) {
            int value = 1 << i;
            int write = -value;
            System.out.println("write sint:" + write);
            CompressBean.writeSint(buf, write);
            int read = CompressBean.readSint(buf);
            System.out.println("read sint:" + read);
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write sint:" + write);
            CompressBean.writeSint(buf, write);
            read = CompressBean.readSint(buf);
            System.out.println("read sint:" + read);
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        int write = Integer.MIN_VALUE;
        System.out.println("write sint:" + write);
        CompressBean.writeSint(buf, write);
        int read = CompressBean.readSint(buf);
        System.out.println("read sint:" + read);
       Assertions.assertEquals(write, read);
        write = Integer.MAX_VALUE - 1;
        System.out.println("write sint:" + write);
        CompressBean.writeSint(buf, write);
        read = CompressBean.readSint(buf);
        System.out.println("read sint:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void slongTest() {

        ByteBuf buf = Unpooled.buffer(1000);
        for (long i = 0; i < 63; i++) {
            long value = 1L << i;
            long write = -value;
            System.out.println("write slong:" + write);
            CompressBean.writeSlong(buf, write);
            long read = CompressBean.readSlong(buf);
            System.out.println("read slong:" + read);
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write slong:" + write);
            CompressBean.writeSlong(buf, write);
            read = CompressBean.readSlong(buf);
            System.out.println("read slong:" + read);
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        long write = Long.MIN_VALUE;
        System.out.println("write slong:" + write);
        CompressBean.writeSlong(buf, write);
        long read = CompressBean.readSlong(buf);
        System.out.println("read slong:" + read);
       Assertions.assertEquals(write, read);
        write = Long.MAX_VALUE;
        System.out.println("write slong:" + write);
        CompressBean.writeSlong(buf, write);
        read = CompressBean.readSlong(buf);
        System.out.println("read slong:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void fixed32Test() {
        ByteBuf buf = Unpooled.buffer(500);
        for (int i = 0; i < 31; i++) {
            int value = 1 << i;
            int write = -value;
            System.out.println("write fixed32:" + write);
            CompressBean.writeFixed32(buf, write);
            int read = CompressBean.readFixed32(buf);
            System.out.println("read fixed32:" + read);
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write fixed32:" + write);
            CompressBean.writeFixed32(buf, write);
            read = CompressBean.readFixed32(buf);
            System.out.println("read fixed32:" + read);
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        int write = Integer.MIN_VALUE;
        System.out.println("write fixed32:" + write);
        CompressBean.writeFixed32(buf, write);
        int read = CompressBean.readFixed32(buf);
        System.out.println("read fixed32:" + read);
       Assertions.assertEquals(write, read);
        write = Integer.MAX_VALUE - 1;
        System.out.println("write fixed32:" + write);
        CompressBean.writeFixed32(buf, write);
        read = CompressBean.readFixed32(buf);
        System.out.println("read fixed32:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void fixed64Test() {
        ByteBuf buf = Unpooled.buffer(1000);
        for (long i = 0; i < 63; i++) {
            long value = 1L << i;
            long write = -value;
            System.out.println("write fixed64:" + write);
            CompressBean.writeFixed64(buf, write);
            long read = CompressBean.readFixed64(buf);
            System.out.println("read fixed64:" + read);
           Assertions.assertEquals(write, read);
            write = value - 1;
            System.out.println("write fixed64:" + write);
            CompressBean.writeFixed64(buf, write);
            read = CompressBean.readFixed64(buf);
            System.out.println("read fixed64:" + read);
           Assertions.assertEquals(write, read);
            System.out.println();
        }
        long write = Long.MIN_VALUE;
        System.out.println("write fixed64:" + write);
        CompressBean.writeFixed64(buf, write);
        long read = CompressBean.readFixed64(buf);
        System.out.println("read fixed64:" + read);
       Assertions.assertEquals(write, read);
        write = Long.MAX_VALUE;
        System.out.println("write fixed64:" + write);
        CompressBean.writeFixed64(buf, write);
        read = CompressBean.readFixed64(buf);
        System.out.println("read fixed64:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void floatTest() {

        ByteBuf buf = Unpooled.buffer(20);
        float write = -789.0126584122f;
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(6);
        System.out.println("write float:" + decimalFormat.format(write));
        CompressBean.writeFloat(buf, write);
        float read = CompressBean.readFloat(buf);
        System.out.println("read float:" + decimalFormat.format(read));
       Assertions.assertEquals(write, read, 0.0001);
        write = 852.366974441255f;

        System.out.println("write float:" + decimalFormat.format(write));
        CompressBean.writeFloat(buf, write);
        read = CompressBean.readFloat(buf);
        System.out.println("read float:" + decimalFormat.format(read));
       Assertions.assertEquals(write, read, 0.0001);
    }

    @Test
    public void doubleTest() {
        ByteBuf buf = Unpooled.buffer(20);
        double write = -789.012650084122D;
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(12);
        System.out.println("write double:" + decimalFormat.format(write));
        CompressBean.writeDouble(buf, write);
        double read = CompressBean.readDouble(buf);
        System.out.println("read double:" + decimalFormat.format(read));
       Assertions.assertEquals(write, read, 0.00000001);
        write = 852.36697004441255D;
        System.out.println("write double:" + decimalFormat.format(write));
        CompressBean.writeDouble(buf, write);
        read = CompressBean.readDouble(buf);
        System.out.println("read double:" + decimalFormat.format(read));
       Assertions.assertEquals(write, read, 0.00000001);
    }

    @Test
    public void booleanTest() {
        ByteBuf buf = Unpooled.buffer(20);
        boolean write = true;
        System.out.println("write boolean:" + write);
        CompressBean.writeBoolean(buf, write);
        boolean read = CompressBean.readBoolean(buf);
        System.out.println("read boolean:" + read);
       Assertions.assertEquals(write, read);
        write = false;
        System.out.println("write boolean:" + write);
        CompressBean.writeBoolean(buf, write);
        read = CompressBean.readBoolean(buf);
        System.out.println("read boolean:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void stringTest() {
        ByteBuf buf = Unpooled.buffer(200);

        String write = "this is a 字符串?!3/dsdfd888=00)k";
        System.out.println("write string:" + write);
        CompressBean.writeString(buf, write);
        String read = CompressBean.readString(buf);
        System.out.println("read string:" + read);
       Assertions.assertEquals(write, read);
    }

    @Test
    public void bytesTest() {
        ByteBuf buf = Unpooled.buffer(200);

        byte[] write=new byte[]{0,1,2,3,127,-2,-128};
        System.out.println("write bytes:" + Arrays.toString(write));
        CompressBean.writeBytes(buf, write);
        byte[] read = CompressBean.readBytes(buf);
        System.out.println("read bytes:" + Arrays.toString(read));
       Assertions.assertEquals( Arrays.toString(write), Arrays.toString(read));
    }

}