package com.senpure.grammar.antlr;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * GrammarSource
 *
 * @author senpure
 * @time 2020-05-16 12:15:08
 */
public class GrammarModel<T extends ParserRuleContext> {
    private T ruleContext;
    private CommonTokenStream tokenStream;
    private String filePath;

    public T getRuleContext() {
        return ruleContext;
    }

    public void setRuleContext(T ruleContext) {
        this.ruleContext = ruleContext;
    }

    public CommonTokenStream getTokenStream() {
        return tokenStream;
    }

    public void setTokenStream(CommonTokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
