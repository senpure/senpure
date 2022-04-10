package com.senpure.grammar.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * FieldModel
 *
 * @author senpure
 * @time 2020-05-16 14:16:30
 */
public class FieldModel {
    private List<String> multilineComments = new ArrayList<>(8);
    private List<String> lineComments = new ArrayList<>(8);
    //private public
    private String accessType = "";
    private boolean staticModifier;
    private boolean finalModifier;
    //int Integer java.lang.Integer
    private String clazzType;
    //age Id
    private String name;

    public String getFieldKey() {
        String key = accessType;
        if (isStaticModifier()) {
            key += "static";
        }
        if (isFinalModifier()) {
            key += "final";
        }
        key += "." + name;
        return key;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(accessType);
        if (isStaticModifier()) {
            sb.append(" static");
        }
        if (isFinalModifier()) {
            sb.append(" final");
        }
        sb.append(" ").append(clazzType);
        sb.append(" ").append(name);
        if (lineComments.size() > 0) {
            sb.append(" lineComments = ").append(lineComments);
        }
        return sb.toString();
    }

    public List<String> getMultilineComments() {
        return multilineComments;
    }

    public void setMultilineComments(List<String> multilineComments) {
        this.multilineComments = multilineComments;
    }

    public List<String> getLineComments() {
        return lineComments;
    }

    public void setLineComments(List<String> lineComments) {
        this.lineComments = lineComments;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getClazzType() {
        return clazzType;
    }

    public void setClazzType(String clazzType) {
        this.clazzType = clazzType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStaticModifier() {
        return staticModifier;
    }

    public void setStaticModifier(boolean staticModifier) {
        this.staticModifier = staticModifier;
    }

    public boolean isFinalModifier() {
        return finalModifier;
    }

    public void setFinalModifier(boolean finalModifier) {
        this.finalModifier = finalModifier;
    }
}
