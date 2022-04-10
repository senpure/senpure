package com.senpure.io.protocol;

/**
 * ComputeTest
 *
 * @author senpure
 * @time 2019-09-19 11:00:59
 */
public class ComputeTest {
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
    public void computeLong() {

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (long i = 0; i < 63; i++) {
            long value = (long) Math.pow(2, i);
            System.out.println(value+" size:"+CompressBean.computeVar64Size(value));
           // sb.append(value);
           // sb.append(",");
            value = value-1;
            System.out.println(value+" size:"+CompressBean.computeVar64Size(value));
            System.out.println();
            sb.append(value);
            sb.append(",");
        }
        System.out.println(Long.MAX_VALUE+" size:"+CompressBean.computeVar64Size(Long.MAX_VALUE));

        sb.append(Long.MAX_VALUE);
        sb.append("]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {

        ComputeTest computeTest = new ComputeTest();
       computeTest.computeLong();

        //System.out.println(CompressBean.computeVar64Size(4294967296L));
        System.out.println(1L<<53);
        long value = (long) Math.pow(2, 53);
        System.out.println(value);
    }
}
