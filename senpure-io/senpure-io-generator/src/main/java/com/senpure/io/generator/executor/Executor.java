package com.senpure.io.generator.executor;

import com.senpure.base.AppEvn;
import com.senpure.base.util.Assert;
import com.senpure.io.generator.habit.JavaScriptConfig;
import com.senpure.io.generator.habit.LanguageConfig;
import com.senpure.io.generator.habit.LuaConfig;
import com.senpure.io.generator.model.Bean;
import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.Event;
import com.senpure.io.generator.model.Message;
import com.senpure.io.generator.reader.IoProtocolReader;
import com.senpure.io.generator.reader.IoReader;
import com.senpure.io.generator.util.CheckUtil;
import com.senpure.io.generator.util.TemplateUtil;
import com.senpure.template.FileUtil;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Executor
 *
 * @author senpure
 * @time 2019-06-11 15:56:05
 */
public class Executor {

    private final Configuration cfg;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ExecutorContext context;

    public Executor(ExecutorContext context) {
        this.context = context;
        CheckUtil.loadData(context.getProjectName());
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        TemplateUtil.share(cfg);
    }


    public boolean check() {
        boolean check = true;
        for (Bean bean : context.getBeans()) {
            boolean temp = CheckUtil.check(bean);
            if (!temp) {
                check = false;
            }
        }
        for (Enum anEnum : context.getEnums()) {
            boolean temp = CheckUtil.check(anEnum);
            if (!temp) {
                check = false;
            }
        }
        for (Message message : context.getMessages()) {
            boolean temp = CheckUtil.check(message);
            if (!temp) {
                check = false;
            }
        }
        for (Event event : context.getEvents()) {
            boolean temp = CheckUtil.check(event);
            if (!temp) {
                check = false;
            }
        }
        return check;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    public void generate() {
        if (!check()) {
            CheckUtil.closeCheck();
            throw new CheckException("检查不通过");
        }
        CheckUtil.checkFlush();
        for (LanguageConfig languageConfig : context.getLanguageConfigs()) {
            LanguageExecutor languageExecutor = languageConfig.languageExecutor();
            changeTemplateDir(languageExecutor.getTemplateDir());
            languageExecutor.generate(cfg, context, languageConfig);
        }
    }

    public void changeTemplateDir(String dir) {
        try {
            cfg.setDirectoryForTemplateLoading(new File(TemplateUtil.templateDir(), dir));
        } catch (IOException e) {
            Assert.error(e);
        }
    }



    public static void main(String[] args) {
        AppEvn.markClassRootPath();
        AppEvn.installAnsiConsole();

        List<String> paths = new ArrayList<>();
        //paths.add("hello.io");
        // paths.add("hello3.io");
        //  paths.add("fixed.io");

        for (String path : paths) {
            IoReader.getInstance().read(FileUtil.file("../../src/main/resources/" + path, AppEvn.getClassRootPath()));
        }
        IoReader.getInstance().read(new File("E:\\Projects\\senpure-io-example\\src\\main\\resources\\example.io"));

        Map<String, IoProtocolReader> ioProtocolReaderMap = IoReader.getInstance().getIoProtocolReaderMap();

        ExecutorContext context = new ExecutorContext();

        context.setProjectName("test");
        for (IoProtocolReader value : ioProtocolReaderMap.values()) {
            context.addBeans(value.getBeans());
            context.addEnums(value.getEnums());
            context.addEvents(value.getEvents());
            context.addMessages(value.getMessages());
        }
        // context.setJavaEventHandlerRootPath(FileUtil.file("../../src/test/java").getAbsolutePath());
        // context.setJavaProtocolCodeRootPath(FileUtil.file("../../src/test/java").getAbsolutePath());

       String path = FileUtil.file("../../src/test/java").getAbsolutePath();
        path="E:\\Projects\\senpure-io-js-support\\senpure";

       // context.setLuaConfig(luaConfig);
        JavaScriptConfig javaScriptConfig = new JavaScriptConfig();
        javaScriptConfig.setProtocolCodeRootPath(path);
        javaScriptConfig.setDtsCodeRootPath(path);
        javaScriptConfig.setGenerateRequire(true);
        javaScriptConfig.setAppendNamespace(true);
        context.addLanguageConfig(javaScriptConfig);
        LuaConfig luaConfig = new LuaConfig();
        luaConfig.setAppendNamespace(true);
        luaConfig.setProtocolCodeRootPath(path);
        context.addLanguageConfig(luaConfig);
        Executor executor = new Executor(context);
        executor.generate();
    }
}
