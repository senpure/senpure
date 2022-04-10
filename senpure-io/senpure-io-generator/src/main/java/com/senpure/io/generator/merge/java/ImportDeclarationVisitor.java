package com.senpure.io.generator.merge.java;

import com.senpure.io.generator.merge.java.antlr.Java8Parser;

/**
 * ImportDeclarationVisitor
 *
 * @author senpure
 * @time 2019-10-09 20:36:43
 */
public class ImportDeclarationVisitor extends ContextVisitor<Java8Parser.ImportDeclarationContext> {

    @Override
    public Java8Parser.ImportDeclarationContext visitImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {


        return ctx;
    }
}
