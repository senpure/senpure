package com.senpure.io.generator.model;


public class Message extends Bean {
    private int id;
    private String type;
    private String javaHandlerPackage;

    @Override
    public String getJavaName() {
        return getType() + getName() + "Message";
    }

    public String getJavaHandlerName() {
        return getType() + getName() + "MessageHandler";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaHandlerPackage() {
        return javaHandlerPackage;
    }

    public void setJavaHandlerPackage(String javaHandlerPackage) {
        this.javaHandlerPackage = javaHandlerPackage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + getType() + '\'' +
                ",name='" + getName() + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
