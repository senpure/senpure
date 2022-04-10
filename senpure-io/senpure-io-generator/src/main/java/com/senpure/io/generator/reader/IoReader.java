package com.senpure.io.generator.reader;

import com.senpure.io.antlr.IoLexer;
import com.senpure.io.antlr.IoParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoReader
 *
 * @author senpure
 * @time 2019-06-10 10:25:45
 */
public class IoReader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static class Inner {
        private static final IoReader ioReader = new IoReader();

    }

    private static Map<String, IoReader> readerMap = new ConcurrentHashMap<>();

    public static IoReader getInstance() {
        return Inner.ioReader;
    }

    public static IoReader getInstance(String identity) {
        IoReader reader = readerMap.get(identity);
        if (reader == null) {
            reader = new IoReader();
            readerMap.putIfAbsent(identity, reader);
            return readerMap.get(identity);
        }
        return reader;
    }


    private volatile Map<String, IoProtocolReader> ioProtocolReaderMap = new HashMap<>();


    public IoProtocolReader read(File file) {
        String key = file.getAbsolutePath();
        if (key.endsWith(".io")) {
            IoProtocolReader last = ioProtocolReaderMap.get(key);
            if (last == null) {
                IoProtocolReader ioProtocolReader = new IoProtocolReader();
                ioProtocolReaderMap.put(key, ioProtocolReader);
                ioProtocolReader.read(file, this);
                return ioProtocolReader;
            }
            return last;

        } else {
            logger.warn("{} 文件后缀格式不对,需要.io", key);
        }

        return null;
    }

    public void clear() {
        ioProtocolReaderMap.clear();
    }

    public void replace(String key, IoProtocolReader ioProtocolReader) {
        ioProtocolReaderMap.put(key, ioProtocolReader);
    }


    public Map<String, IoProtocolReader> getIoProtocolReaderMap() {
        return ioProtocolReaderMap;
    }

    public static void main(String[] args) {
       // AppEvn.markClassRootPath();
      //  AppEvn.installAnsiConsole();
        //IoReader.getInstance().read(new File(AppEvn.getClassRootPath(), "hello.io"));

        String str="bean IdName {\n" +
                "    int    id          = 1;               //消息id\n" +
                "    String messageName = 2;               //有意义的字符串\n" +
                "}\n";
        str=" int    id          = 1//8888";
        CharStream input=  CharStreams.fromString(str);
        Lexer lexer = new IoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);



        IoParser parser = new IoParser(tokens);


        System.out.println("--------");
        System.out.println(parser.field().getText());
        System.out.println("--------");

    }

}
