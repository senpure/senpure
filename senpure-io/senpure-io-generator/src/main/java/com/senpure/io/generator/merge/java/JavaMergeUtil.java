package com.senpure.io.generator.merge.java;

import com.senpure.base.util.Assert;
import com.senpure.io.generator.merge.InsertUtil;
import com.senpure.io.generator.merge.java.antlr.Java8Parser;
import com.senpure.io.generator.merge.java.model.ClassModel;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * MergeUtil
 *
 * @author senpure
 * @time 2019-10-09 20:30:16
 */
public class JavaMergeUtil {

    private static Logger logger = LoggerFactory.getLogger(JavaMergeUtil.class);

    /**
     * 将data合并到root
     *
     * @param root
     * @param data
     */
    public static int merge(String root, String data) throws IOException {
        return merge(root, data, 0);
    }

    private static int merge(String root, String data, int index) throws IOException {
        List<ClassModel> rootClassModels = readClassModel(root);
        List<ClassModel> dataClassModels = readClassModel(data);
        int dataLength = 0;
        if (index < rootClassModels.size()) {
            dataLength = mergeMethod(rootClassModels.get(index), dataClassModels.get(index));
            if (index + 1 < rootClassModels.size()) {
                return dataLength + merge(root, data, index + 1);
            }
        }
        return dataLength;

    }

    public static List<ClassModel> readClassModel(String filePath) throws IOException {
        JavaReader rootReader = new JavaReader();
        Java8Parser.CompilationUnitContext rootContext = rootReader.read(filePath);

//        logger.info(rootContext.getStop().getStopIndex()+"");
//        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
//        byte [] bytes=new byte[(int) randomAccessFile.length()];
//        randomAccessFile.read(bytes);
//        logger.info("{} {}", randomAccessFile.length(), new String(bytes, "utf-8").length());

        List<ClassModel> classModels = new ArrayList<>();
        for (Java8Parser.TypeDeclarationContext typeDeclarationContext : rootContext.typeDeclaration()) {
            ClassModel classModel = new ClassModel();
            classModel.setFilePath(filePath);
            classModel.setTokens(rootReader.getTokens());

            classModel.setTypeDeclarationContext(typeDeclarationContext);
            readClassModel(classModel, typeDeclarationContext);
            classModels.add(classModel);


        }
        return classModels;
    }

    private static void readClassModel(ClassModel classModel, Java8Parser.TypeDeclarationContext typeDeclarationContext) {
        if (typeDeclarationContext.classDeclaration().normalClassDeclaration() == null) {

            Assert.error("不是普通类,不支持");
        }
        Java8Parser.ClassBodyContext classBodyContext = typeDeclarationContext.classDeclaration().normalClassDeclaration().classBody();

        classModel.setClassBodyContext(classBodyContext);
        for (Java8Parser.ClassBodyDeclarationContext classBodyDeclarationContext : classBodyContext.classBodyDeclaration()) {
            // classModel.setClassBodyDeclarationContext(classBodyDeclarationContext);
            Java8Parser.ClassMemberDeclarationContext classMemberDeclarationContext = classBodyDeclarationContext.classMemberDeclaration();

            if (classMemberDeclarationContext != null) {
                Java8Parser.MethodDeclarationContext tempMethod = classMemberDeclarationContext.methodDeclaration();
                if (tempMethod != null) {
                    classModel.getMethodDeclarationContexts().add(tempMethod);

//                    try {
//                        readSourceBytes(new RandomAccessFile(classModel.getFilePath(), "r"), classModel.getTokens(), tempMethod, 0);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                Java8Parser.FieldDeclarationContext tempField = classMemberDeclarationContext.fieldDeclaration();
                if (tempField != null) {
                    classModel.getFieldDeclarationContexts().add(tempField);
                }
            }

            Java8Parser.ConstructorDeclarationContext tempConstructor = classBodyDeclarationContext.constructorDeclaration();
            if (tempConstructor != null) {
                classModel.getConstructorDeclarationContexts().add(tempConstructor);
                if (tempConstructor.constructorDeclarator().formalParameterList() == null) {
                    //  logger.info(tempConstructor.getText());
                    classModel.setNoParametersConstructorDeclarationContext(tempConstructor);
                }
            }

        }
    }
    // RandomAccessFile randomAccessFile=new RandomAccessFile()

    public static int mergeMethod(ClassModel rootClassModel, ClassModel dataClassModel) throws IOException {
        List<Java8Parser.MethodDeclarationContext> adds = new ArrayList<>();
        List<String> dataMethods = getMethodSigns(dataClassModel);
        List<String> rootMethods = getMethodSigns(rootClassModel);
        for (int i = 0; i < dataMethods.size(); i++) {
            String dataSign = dataMethods.get(i);
            boolean find = false;
            for (String rootMethod : rootMethods) {
                if (dataSign.equals(rootMethod)) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                adds.add(dataClassModel.getMethodDeclarationContexts().get(i));
            }
        }
        if (adds.size() > 0) {
            RandomAccessFile rootFile = new RandomAccessFile(rootClassModel.getFilePath(), "rw");
            int addBytesLength = 0;
            RandomAccessFile dataFile = new RandomAccessFile(dataClassModel.getFilePath(), "r");
            BytePosition readPosition;
            if (rootMethods.size() > 0) {
                Java8Parser.MethodDeclarationContext methodDeclarationContext = rootClassModel.getMethodDeclarationContexts().get(0);
                readPosition = getStartBytePosition(rootFile, rootClassModel.getTokens(), methodDeclarationContext);

            } else {
                int size = rootClassModel.getFieldDeclarationContexts().size();
                if (size > 0) {
                    Java8Parser.FieldDeclarationContext fieldDeclarationContext =
                            rootClassModel.getFieldDeclarationContexts().get(size - 1);
                    readPosition = getBytePosition(rootFile, fieldDeclarationContext.getStop().getLine(), fieldDeclarationContext.getStop()
                            .getCharPositionInLine() + 1);
                    byte[] bytes = "\n\n    ".getBytes(StandardCharsets.UTF_8);
                    addBytesLength += bytes.length;
                    InsertUtil.insertBytes(rootFile, readPosition.position, bytes);
                    readPosition.bytePositionInLine = 4;
                } else {
                    readPosition = getBytePosition(rootFile, rootClassModel.getClassBodyContext().getStart().getLine(),
                            rootClassModel.getClassBodyContext().getStart().getCharPositionInLine() + 1);
                    byte[] bytes = "\n\n    ".getBytes(StandardCharsets.UTF_8);
                    addBytesLength += bytes.length;
                    InsertUtil.insertBytes(rootFile, readPosition.position, bytes);
                    readPosition.bytePositionInLine = 4;
                }


            }

            for (Java8Parser.MethodDeclarationContext declarationContext : adds) {
                byte[] bytes = readSourceBytes(dataFile, dataClassModel.getTokens(),
                        declarationContext, 2, readPosition.bytePositionInLine);
               // logger.info("\n[{}]", declarationContext.getText());
                logger.info("\n[{}]",new String(bytes,"utf-8"));
                InsertUtil.insertBytes(rootFile, readPosition.position + addBytesLength, bytes);
                addBytesLength += bytes.length;
            }
            rootFile.close();
            dataFile.close();
            return addBytesLength;
        }
        return 0;
    }

    private static TokenPosition getStartTokenPosition(CommonTokenStream tokens, ParserRuleContext context) {
        TokenPosition tokenPosition = new TokenPosition();

        List<Token> hiddenTokens = tokens.getHiddenTokensToLeft(context.getStart().getTokenIndex());
        if (hiddenTokens != null) {
//            logger.info("111111111111111");
//            for (Token hiddenToken : hiddenTokens) {
//                logger.info(hiddenToken.getText());
//            }
//            logger.info("2222222222222");
            tokenPosition.line = hiddenTokens.get(0).getLine();
            tokenPosition.charPositionInLine = hiddenTokens.get(0).getCharPositionInLine();
        } else {
            tokenPosition.line = context.getStart().getLine();
            tokenPosition.charPositionInLine = context.getStart().getCharPositionInLine();
        }
        return tokenPosition;
    }

    public static BytePosition getStartBytePosition(RandomAccessFile accessFile, CommonTokenStream tokens, ParserRuleContext context) throws IOException {
        TokenPosition tokenPosition = getStartTokenPosition(tokens, context);
        return getBytePosition(accessFile, tokenPosition.line, tokenPosition.charPositionInLine);
    }

    public static BytePosition getBytePosition(RandomAccessFile accessFile, int charLine, int charPositionInLine) throws IOException {
        int line = 1;
        long position = 0;
        // long length = dataFile.length();
        accessFile.seek(position);
        while (line < charLine) {
            accessFile.readLine();
            line++;
        }
        position = accessFile.getFilePointer();
        //logger.info("position = {},length {}", position, dataFile.length());
        String lineStr = accessFile.readLine();
        // logger.info("lineStr:{}", lineStr);
        BytePosition readPosition = new BytePosition();

        byte[] lineBytes = lineStr.substring(0, charPositionInLine).getBytes(StandardCharsets.UTF_8);
        readPosition.position = (int) (position + lineBytes.length);
        readPosition.bytePositionInLine = lineBytes.length;
        return readPosition;
    }

    public static byte[] readSourceBytes(RandomAccessFile accessFile, CommonTokenStream tokens,
                                         ParserRuleContext context, int endLines, int endSpaces) throws IOException {
        TokenPosition tokenPosition = getStartTokenPosition(tokens, context);
        int startCharPositionInLine = tokenPosition.charPositionInLine;
        int startLine = tokenPosition.line;
        accessFile.seek(0);
        int line = 1;
        long position;
        // long length = dataFile.length();
        while (line < startLine) {
            accessFile.readLine();
            line++;
        }
        position = accessFile.getFilePointer();
        //logger.info("position = {},length {}", position, dataFile.length());
        String lineStr = accessFile.readLine();
        // logger.info("lineStr:{}", lineStr);
        byte[] lineBytes = lineStr.substring(0, startCharPositionInLine).getBytes(StandardCharsets.UTF_8);

        position += lineBytes.length;
        long startPosition = position;
        accessFile.seek(position);

        int endLine = context.getStop().getLine();
        int endCharPositionInLine = context.getStop().getCharPositionInLine();
        while (line < endLine) {
            accessFile.readLine();
            line++;
        }
        position = (int) accessFile.getFilePointer();
        lineStr = accessFile.readLine();
        //logger.info("lineStr:{}  length:{} -> endCharPositionInLine:{}", lineStr, lineStr.length(), endCharPositionInLine);
        if (startLine == endLine) {
            endCharPositionInLine -= startCharPositionInLine;
        }
        lineBytes = lineStr.substring(0, endCharPositionInLine).getBytes(StandardCharsets.UTF_8);
        position += lineBytes.length;
        byte[] bytes = new byte[(int) (position - startPosition + 1 + endLines + endSpaces)];
        accessFile.seek(startPosition);
        accessFile.read(bytes, 0, bytes.length - endLines-endSpaces);
        for (int i = 0; i < endSpaces; i++) {
            bytes[bytes.length - 1 - i] = ' ';
        }
        for (int i = 0; i < endLines; i++) {
            bytes[bytes.length - endSpaces - 1 - i] = '\n';
        }
        //logger.info("\n[{}]", new String(bytes));
        return bytes;

    }

    public static List<String> getMethodSigns(ClassModel classModel) {
        List<String> methods = new ArrayList<>();
        for (Java8Parser.MethodDeclarationContext methodDeclarationContext : classModel.getMethodDeclarationContexts()) {
            String methodSign = getMethodSign(methodDeclarationContext);
            methods.add(methodSign);
        }

        return methods;
    }

    /**
     * 获取方法签名
     *
     * @return
     */
    public static String getMethodSign(Java8Parser.MethodDeclarationContext methodDeclarationContext) {

        StringBuilder sb = new StringBuilder();
        //只需要方法名和参数类型列表
//            for (Java8Parser.MethodModifierContext methodModifierContext : methodDeclarationContext.methodModifier()) {
//                sb.append(methodModifierContext.getText());
//            }
        Java8Parser.MethodHeaderContext methodHeaderContext = methodDeclarationContext.methodHeader();
        sb.append(methodHeaderContext.methodDeclarator().Identifier().getText());
        sb.append("(");
        Java8Parser.FormalParameterListContext parameterListContext = methodHeaderContext.methodDeclarator().formalParameterList();
        if (parameterListContext != null) {

            Java8Parser.ReceiverParameterContext receiverParameterContext = parameterListContext.receiverParameter();
            Java8Parser.FormalParametersContext formalParametersContext = null;
            if (receiverParameterContext != null) {
                sb.append(receiverParameterContext.unannType().getText());
            } else {
                formalParametersContext = parameterListContext.formalParameters();
            }
            if (formalParametersContext != null) {
                for (Java8Parser.FormalParameterContext context : formalParametersContext.formalParameter()) {
                    sb.append(context.unannType().getText());
                    sb.append(",");
                }
                if (parameterListContext.lastFormalParameter().unannType() != null) {
                    sb.append(parameterListContext.lastFormalParameter().unannType().getText());
                } else {
                    sb.append(parameterListContext.lastFormalParameter().formalParameter().unannType().getText());
                }

            } else {
                sb.append(parameterListContext.lastFormalParameter().formalParameter().unannType().getText());
            }
        }


        sb.append(")");

        return sb.toString();
    }

    public static class TokenPosition {
        public int line;
        public int charPositionInLine;
    }

    public static class BytePosition {
        public int position;
        public int bytePositionInLine;
    }

    public static void main(String[] args) throws IOException {
        File root = new File("E:\\Projects\\senpure\\senpure-io\\senpure-io-generator\\src\\test\\java\\Data\\A.java");
        File data = new File("E:\\Projects\\senpure\\senpure-io\\senpure-io-generator\\src\\test\\java\\Data\\B.java");
        // root = new File("E:\\Projects\\senpure-orange-prot-support\\target\\classes\\ww\\port\\LandPort.java");
        //  data = new File("E:\\Projects\\senpure-orange-prot-support\\target\\classes\\ww\\port\\LandPort2.java");
        // File temp=root;
        //  root=data;
        // data=temp;
        JavaMergeUtil.merge(root.getAbsolutePath(), data.getAbsolutePath());

        // readClassModel(data.getAbsolutePath());
    }
}
