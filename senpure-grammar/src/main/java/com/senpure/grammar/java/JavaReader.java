package com.senpure.grammar.java;

import com.senpure.base.util.Assert;
import com.senpure.grammar.antlr.GrammarErrorListener;
import com.senpure.grammar.antlr.GrammarModel;
import com.senpure.grammar.antlr.java.Java8Lexer;
import com.senpure.grammar.antlr.java.Java8Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JavaReader
 *
 * @author senpure
 * @time 2020-05-16 12:12:16
 */
public class JavaReader {
    private Logger logger = LoggerFactory.getLogger(getClass());


    public GrammarModel<Java8Parser.CompilationUnitContext> read(String filePath) {
        GrammarErrorListener errorListener = new GrammarErrorListener(filePath);
        Lexer lexer = null;
        try {
            lexer = new Java8Lexer(CharStreams.fromFileName(filePath));
        } catch (IOException e) {
            Assert.error(e);
        }
        // InputStream inputStream = new FileInputStream(filePath);
        // Lexer lexer = new Java8Lexer(CharStreams.fromStream(inputStream));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        // parser.getErrorListeners().clear();
        parser.addErrorListener(errorListener);
        Java8Parser.CompilationUnitContext context = parser.compilationUnit();
        if (errorListener.isSyntaxError()) {
            Assert.error("语法错误,暂不支持的语法");
        }
        GrammarModel<Java8Parser.CompilationUnitContext> grammarModel = new GrammarModel<>();
        grammarModel.setFilePath(filePath);
        grammarModel.setRuleContext(context);
        grammarModel.setTokenStream(tokens);
        return grammarModel;
        //  TypeDeclarationVisitor  visitor= new TypeDeclarationVisitor();
        // List< Java8Parser.ClassDeclarationContext> classDeclarationContexts=visitor.visit(context);

    }


}
