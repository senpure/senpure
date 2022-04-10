package com.senpure.io.generator.merge;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * InsertUtil
 *
 * @author senpure
 * @time 2019-10-09 10:04:14
 */
public class InsertUtil {


    public static void insertBytes(RandomAccessFile accessFile, int position, byte[] bytes) throws IOException {

        accessFile.setLength(accessFile.length() + bytes.length);

        long start = position + bytes.length;
        accessFile.seek(position);
        for (long i = accessFile.length() - 1; i >= start; i--) {
            accessFile.seek(i - bytes.length);
            byte data = accessFile.readByte();
            accessFile.seek(i);
            accessFile.write(data);
        }
        accessFile.seek(position);
        accessFile.write(bytes);

    }

    public static void main(String[] args) throws IOException {
        File root = new File("E:\\Projects\\senpure\\senpure-io\\senpure-io-generator\\src\\test\\java\\Data\\A.java");
        String str = "int i = 1;\n int  a=0 ;\n\n";
        InsertUtil.insertBytes(new RandomAccessFile(root.getAbsolutePath(), "rw"),
                109, str.getBytes("utf-8"));
    }
}
