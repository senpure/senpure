package com.senpure.io.generator.merge.java;

import com.senpure.base.util.Assert;
import com.senpure.io.generator.merge.java.antlr.Java8Lexer;
import com.senpure.io.generator.merge.java.antlr.Java8Parser;
import com.senpure.io.generator.reader.IoErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * JavaReader
 *
 * @author senpure
 * @time 2019-10-09 13:47:05
 */
public class JavaReader {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private CommonTokenStream tokens;

    public Java8Parser.CompilationUnitContext read(String filePath) throws IOException {
        IoErrorListener ioErrorListener = new IoErrorListener(filePath);

        Lexer lexer = new Java8Lexer(CharStreams.fromFileName(filePath));
        // InputStream inputStream = new FileInputStream(filePath);
        // Lexer lexer = new Java8Lexer(CharStreams.fromStream(inputStream));
        tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        // parser.getErrorListeners().clear();
        parser.addErrorListener(ioErrorListener);
        Java8Parser.CompilationUnitContext context = parser.compilationUnit();
        if (ioErrorListener.isSyntaxError()) {
            Assert.error("语法错误,暂不支持的语法");
        }


        return context;
        //  TypeDeclarationVisitor  visitor= new TypeDeclarationVisitor();
        // List< Java8Parser.ClassDeclarationContext> classDeclarationContexts=visitor.visit(context);

    }

    public CommonTokenStream getTokens() {
        return tokens;
    }

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Administrator\\Desktop\\Boot.java");
        file = new File("E:\\Projects\\senpure\\senpure-io\\senpure-io-generator\\src\\main\\java\\com\\senpure\\io\\generator\\Boot.java");
        JavaReader javaReader = new JavaReader();
        //Java8Parser.CompilationUnitContext context=  javaReader.read(file);


        // context.accept(new ImportDeclarationVisitor());
    }
}
