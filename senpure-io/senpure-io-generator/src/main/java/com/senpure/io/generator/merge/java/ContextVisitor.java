package com.senpure.io.generator.merge.java;

import com.senpure.io.generator.merge.java.antlr.Java8ParserBaseVisitor;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * ContextVisitor
 *
 * @auhor senpure
 * @time 2019-10-09 20:07:22
 */
public class ContextVisitor<T> extends Java8ParserBaseVisitor<T> {
    @Override
    protected boolean shouldVisitNextChild(RuleNode node, T currentResult) {
        return currentResult==null;
    }
}
