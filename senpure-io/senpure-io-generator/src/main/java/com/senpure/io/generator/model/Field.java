package com.senpure.io.generator.model;

public class Field {


    private boolean hasExplain;
    private String explain;
    //协议文件中的类型
    private String fieldType;

    private String name;
    private boolean baseField;
    private boolean list;
    private int capacity=16;

    //是否是字节数据
    private boolean bytes;
    //list下是否打包
    private boolean listPacked;
    //自定义对象时的信息
    private Bean bean;

    private String javaType;
    private int index=0;
    private int wireType;
    private int tag;
    private Location typeLocation=new Location();
    private Location nameLocation = new Location();
    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
        if (explain != null && explain.trim().length() > 0) {
            hasExplain = true;
        }
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBaseField() {
        return baseField;
    }

    public void setBaseField(boolean baseField) {
        this.baseField = baseField;
    }

    public boolean isHasExplain() {
        return hasExplain;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

//    public String getOriginalClassType() {
//        return originalClassType;
//    }
//
//    public void setOriginalClassType(String originalClassType) {
//        this.originalClassType = originalClassType;
//    }



    public Location getTypeLocation() {
        return typeLocation;
    }

    public void setTypeLocation(Location typeLocation) {
        this.typeLocation = typeLocation;
    }

    public Location getNameLocation() {
        return nameLocation;
    }

    public void setNameLocation(Location nameLocation) {
        this.nameLocation = nameLocation;
    }

    public void setHasExplain(boolean hasExplain) {
        this.hasExplain = hasExplain;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public int getWireType() {
        return wireType;
    }

    public void setWireType(int wireType) {
        this.wireType = wireType;
    }

    public boolean isBytes() {
        return bytes;
    }

    public void setBytes(boolean bytes) {
        this.bytes = bytes;
    }

    public boolean isListPacked() {
        return listPacked;
    }

    public void setListPacked(boolean listPacked) {
        this.listPacked = listPacked;
    }

    @Override
    public String toString() {
        return "Field{" +
                "hasExplain=" + hasExplain +
                ", explain='" + explain + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", name='" + name + '\'' +
                ", baseField=" + baseField +
                ", list=" + list +
                ", capacity=" + capacity +
                '}';
    }
}
