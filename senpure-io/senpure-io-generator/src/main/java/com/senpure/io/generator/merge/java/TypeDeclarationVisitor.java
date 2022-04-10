package com.senpure.io.generator.merge.java;

import com.senpure.io.generator.merge.java.antlr.Java8Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassContext
 *
 * @author senpure
 * @time 2019-10-09 17:58:26
 */
public class TypeDeclarationVisitor extends ContextVisitor<List<Java8Parser.ClassDeclarationContext>> {


    @Override
    public List<Java8Parser.ClassDeclarationContext> visitTypeDeclaration(Java8Parser.TypeDeclarationContext ctx) {

        List<Java8Parser.ClassDeclarationContext> list = new ArrayList<>();

        for (ParseTree child : ctx.children) {
            if (Java8Parser.ClassDeclarationContext.class.isInstance(child)) {
                list.add(Java8Parser.ClassDeclarationContext.class.cast(child));
            }
        }
        return list;
    }


}
