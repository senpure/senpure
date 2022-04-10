package com.senpure.grammar.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaModel
 *
 * @author senpure
 * @time 2020-05-16 14:13:37
 */
public class JavaModel {
    private String filePath;
    private String packageName;
    private List<String> imports = new ArrayList<>(32);
    //主class
    private ClassModel classModel;
    private List<ClassModel> outModels = new ArrayList<>(8);

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<ClassModel> getOutModels() {
        return outModels;
    }

    public void setOutModels(List<ClassModel> outModels) {
        this.outModels = outModels;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
    }
}
