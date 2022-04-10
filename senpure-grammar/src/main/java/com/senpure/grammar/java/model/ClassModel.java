package com.senpure.grammar.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassModel
 *
 * @author senpure
 * @time 2020-05-16 14:14:30
 */
public class ClassModel {
    private boolean main = true;
    private boolean inner;
    private String name;
    private List<FieldModel> fields = new ArrayList<>();

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean inner) {
        this.inner = inner;
    }
}
