package com.senpure.io.generator.merge.java.model;

import com.senpure.io.generator.merge.java.antlr.Java8Parser;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassModel
 *
 * @author senpure
 * @time 2019-10-09 21:16:49
 */
public class ClassModel {
   private  String filePath;
    private  CommonTokenStream tokens;
   private  Java8Parser.TypeDeclarationContext typeDeclarationContext;

    private  Java8Parser.ClassBodyContext classBodyContext;
    private Java8Parser.ConstructorDeclarationContext noParametersConstructorDeclarationContext;
    private List<Java8Parser.ConstructorDeclarationContext> constructorDeclarationContexts = new ArrayList<>();

    private List<Java8Parser.FieldDeclarationContext> fieldDeclarationContexts = new ArrayList<>();
    private List<Java8Parser.MethodDeclarationContext> methodDeclarationContexts = new ArrayList<>(16);

    public List<Java8Parser.MethodDeclarationContext> getMethodDeclarationContexts() {
        return methodDeclarationContexts;
    }

    public void setMethodDeclarationContexts(List<Java8Parser.MethodDeclarationContext> methodDeclarationContexts) {
        this.methodDeclarationContexts = methodDeclarationContexts;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }


    public Java8Parser.TypeDeclarationContext getTypeDeclarationContext() {
        return typeDeclarationContext;
    }

    public void setTypeDeclarationContext(Java8Parser.TypeDeclarationContext typeDeclarationContext) {
        this.typeDeclarationContext = typeDeclarationContext;
    }



    public Java8Parser.ConstructorDeclarationContext getNoParametersConstructorDeclarationContext() {
        return noParametersConstructorDeclarationContext;
    }

    public void setNoParametersConstructorDeclarationContext(Java8Parser.ConstructorDeclarationContext noParametersConstructorDeclarationContext) {
        this.noParametersConstructorDeclarationContext = noParametersConstructorDeclarationContext;
    }

    public List<Java8Parser.ConstructorDeclarationContext> getConstructorDeclarationContexts() {
        return constructorDeclarationContexts;
    }

    public void setConstructorDeclarationContexts(List<Java8Parser.ConstructorDeclarationContext> constructorDeclarationContexts) {
        this.constructorDeclarationContexts = constructorDeclarationContexts;
    }

    public Java8Parser.ClassBodyContext getClassBodyContext() {
        return classBodyContext;
    }

    public void setClassBodyContext(Java8Parser.ClassBodyContext classBodyContext) {
        this.classBodyContext = classBodyContext;
    }

    public List<Java8Parser.FieldDeclarationContext> getFieldDeclarationContexts() {
        return fieldDeclarationContexts;
    }

    public void setFieldDeclarationContexts(List<Java8Parser.FieldDeclarationContext> fieldDeclarationContexts) {
        this.fieldDeclarationContexts = fieldDeclarationContexts;
    }

    public CommonTokenStream getTokens() {
        return tokens;
    }

    public void setTokens(CommonTokenStream tokens) {
        this.tokens = tokens;
    }
}

