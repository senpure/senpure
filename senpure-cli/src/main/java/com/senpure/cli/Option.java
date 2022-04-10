package com.senpure.cli;

public class Option {
    private final String value;
    private final String display;
    private final String group;
    private final String description;
    private final String suffix;
    private final String key;
    private final boolean complete;
    private final int sort;


    public Option(String value) {
        this(value, value, null, null, null, null, true, 0);
    }

    public Option(String value, boolean complete) {
        this(value, value, null, null, null, null, complete, 0);
    }

    public Option(String value, String display, String group, String description, String suffix, String key, boolean complete, int sort) {
        this.value = value;
        this.display = display;
        this.group = group;
        this.description = description;
        this.suffix = suffix;
        this.key = key;
        this.complete = complete;
        this.sort = sort;
    }

    public String getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    public String getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getKey() {
        return key;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getSort() {
        return sort;
    }
}
