package com.senpure.io.generator.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ProtocolUtil {
    public static final int WIRE_TYPE_VARINT = 0;
    public static final int WIRE_TYPE_FIXED32 = 1;
    public static final int WIRE_TYPE_FIXED64 = 2;
    public static final int WIRE_TYPE_LENGTH_DELIMITED = 3;
    public static final String BYTES_FIELD_TYPE = "bytes";
    public static String[] baseFields = {"int", "long", "sint", "slong", "fixed32", "fixed64", "float", "double", "boolean", "string", "String", "byte", "short", "bytes"};

    /**
     * 不含兼容
     */
    public static String[] standardBaseFields = {"int", "long", "sint", "slong", "fixed32", "fixed64", "float", "double", "boolean", "String",  "bytes"};

    public static Map<String, Integer> WIRE_TYPE = new HashMap<>();
    public static Map<String, String> JAVA_TYPE = new HashMap<>();
    public static Set<String> LIST_PACKED = new HashSet<>();

    static {
        WIRE_TYPE.put("int", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("long", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("byte", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("short", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("boolean", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("sint", WIRE_TYPE_VARINT);
        WIRE_TYPE.put("slong", WIRE_TYPE_VARINT);

        WIRE_TYPE.put("float", WIRE_TYPE_FIXED32);
        WIRE_TYPE.put("fixed32", WIRE_TYPE_FIXED32);


        WIRE_TYPE.put("double", WIRE_TYPE_FIXED64);
        WIRE_TYPE.put("fixed64", WIRE_TYPE_FIXED64);
        WIRE_TYPE.put("string", WIRE_TYPE_LENGTH_DELIMITED);
        WIRE_TYPE.put("String", WIRE_TYPE_LENGTH_DELIMITED);
        WIRE_TYPE.put("bytes", WIRE_TYPE_LENGTH_DELIMITED);

        JAVA_TYPE.put("int", "int");
        JAVA_TYPE.put("long", "long");
        JAVA_TYPE.put("sint", "int");
        JAVA_TYPE.put("slong", "long");
        JAVA_TYPE.put("byte", "int");
        JAVA_TYPE.put("short", "int");

        JAVA_TYPE.put("fixed32", "int");
        JAVA_TYPE.put("fixed64", "long");


        JAVA_TYPE.put("boolean", "boolean");
        JAVA_TYPE.put("String", "String");
        JAVA_TYPE.put("string", "String");
        JAVA_TYPE.put("float", "float");
        JAVA_TYPE.put("double", "double");
        JAVA_TYPE.put("bytes", "byte []");

        LIST_PACKED.add("int");
        LIST_PACKED.add("long");
        LIST_PACKED.add("sint");
        LIST_PACKED.add("slong");
        LIST_PACKED.add("byte");
        LIST_PACKED.add("short");
        LIST_PACKED.add("fixed32");
        LIST_PACKED.add("fixed64");
        LIST_PACKED.add("boolean");
        LIST_PACKED.add("float");
        LIST_PACKED.add("double");

    }


    public static int getWireType(String type) {

        return WIRE_TYPE.get(type);
    }

    public static String getJavaType(String type) {

        return JAVA_TYPE.get(type);
    }

    public static boolean isBaseField(String type) {
        for (String str : baseFields) {
            if (str.equals(type)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isListPacked(String type) {
        return LIST_PACKED.contains(type);
    }

    public static void main(String[] args) {
        System.out.println("".getBytes().length);
    }
}
