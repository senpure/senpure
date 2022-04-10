package com.senpure.io.generator.executor;

import com.senpure.base.util.Assert;
import com.senpure.io.generator.habit.JavaConfig;
import com.senpure.io.generator.model.Bean;
import com.senpure.io.generator.model.Event;
import com.senpure.io.generator.model.Message;
import com.senpure.template.FileUtil;
import com.senpure.template.Generator;
import com.senpure.template.sovereignty.Sovereignty;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;

/**
 * JavaExecutor
 *
 * @author senpure
 * @time 2019-09-25 16:41:16
 */
public class JavaExecutor extends AbstractLanguageExecutor<JavaConfig> {
    public static final String TEMPLATE_DIR = "java";
    protected Configuration cfg;
    protected ExecutorContext context;
    protected JavaConfig javaConfig;

    @Override
    public void generate(Configuration cfg, ExecutorContext context, JavaConfig javaConfig) {
        this.cfg = cfg;
        this.context = context;
        this.javaConfig = javaConfig;
        if (javaConfig.isGenerateBean()) {
            generateJavaEnum();
            generateJavaBean();
        }
        if (javaConfig.isGenerateMessage()) {
            generateJavaMessage();
        }
        if (javaConfig.isGenerateCSMessageHandler()) {
            generateJavaCSMessageHandler();
        }
        if (javaConfig.isGenerateSCMessageHandler()) {
            generateJavaSCMessageHandler();
        }
        if (javaConfig.isGenerateEvent()) {
            generateJavaEvent();
        }
        if (javaConfig.isGenerateEventHandler()) {
            generateJavaEventHandler();
        }
    }
    public void generateJavaBean() {
        Template template = null;
        try {
            template = cfg.getTemplate(javaConfig.getBeanTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Bean bean : context.getBeans()) {
            File file = new File(javaConfig.getProtocolCodeRootPath(), FileUtil.fullFileEnd(bean.getJavaPackage().replace(".", File.separator)) + bean.getJavaName() + ".java");
            checkFile(file);
            bean.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
            logger.info("生成 bean {} {}", file.getName(), file.getAbsoluteFile());
            Generator.generate(bean, template, file);
        }

    }

    private void generateJavaEnum() {

        Template template = null;
        try {
            template = cfg.getTemplate(javaConfig.getEnumTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Bean bean : context.getEnums()) {
            File file = new File(javaConfig.getProtocolCodeRootPath(), FileUtil.fullFileEnd(bean.getJavaPackage().replace(".", File.separator)) + bean.getJavaName() + ".java");
            checkFile(file);
            bean.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
            logger.info("生成 enum {} {}", file.getName(), file.getAbsoluteFile());
            Generator.generate(bean, template, file);
        }

    }


    private void generateJavaMessage() {

        Template template = null;
        try {
            template = cfg.getTemplate(javaConfig.getMessageTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Bean bean : context.getMessages()) {
            File file = new File(javaConfig.getProtocolCodeRootPath(), FileUtil.fullFileEnd(bean.getJavaPackage().replace(".", File.separator)) + bean.getJavaName() + ".java");
            checkFile(file);
            bean.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
            logger.info("生成 message {} {}", file.getName(), file.getAbsoluteFile());
            Generator.generate(bean, template, file);
        }

    }

    private void generateJavaMessageHandler(String type, String templateName, String codeRootPath, boolean fileCover) {

        Template template = null;
        try {
            template = cfg.getTemplate(templateName, "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Message message : context.getMessages()) {
            if (message.getType().equals(type)) {
                File file = new File(codeRootPath, FileUtil.fullFileEnd(message.getJavaHandlerPackage().replace(".", File.separator)) + message.getJavaHandlerName() + ".java");
                boolean cover = false;
                if (file.exists()) {
                    if (!fileCover) {
                        logger.warn("messageHandler 存在不能生成 {} {}", file.getName(), file.getAbsoluteFile());
                        continue;
                    } else {
                        cover = true;
                    }
                } else {
                    checkFile(file);
                }
                message.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
                if (cover) {
                    logger.info("覆盖生成 messageHandler {} {}", file.getName(), file.getAbsoluteFile());
                } else {
                    logger.info("生成 messageHandler {} {}", file.getName(), file.getAbsoluteFile());
                }
                Generator.generate(message, template, file);
            }
        }
    }

    private void generateJavaCSMessageHandler() {

        generateJavaMessageHandler("CS",
                javaConfig.getCsMessageHandlerTemplate(),
                javaConfig.getCsMessageHandlerCodeRootPath(),
                javaConfig.isCsMessageHandlerOverwrite());
    }

    private void generateJavaSCMessageHandler() {

        generateJavaMessageHandler("SC",
                javaConfig.getScMessageHandlerTemplate(),
                javaConfig.getScMessageHandlerCodeRootPath(),
                javaConfig.isScMessageHandlerOverwrite());
    }

    private void generateJavaEvent() {

        Template template = null;
        try {
            template = cfg.getTemplate(javaConfig.getEventTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Bean bean : context.getEvents()) {
            File file = new File(javaConfig.getProtocolCodeRootPath(), FileUtil.fullFileEnd(bean.getJavaPackage().replace(".", File.separator)) + bean.getJavaName() + ".java");
            checkFile(file);
            bean.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
            logger.info("生成 event {} {}", file.getName(), file.getAbsoluteFile());
            Generator.generate(bean, template, file);
        }

    }

    private void generateJavaEventHandler() {

        Template template = null;
        try {
            template = cfg.getTemplate(javaConfig.getEventHandlerTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        for (Event event : context.getEvents()) {
            File file = new File(javaConfig.getEventHandlerCodeRootPath(), FileUtil.fullFileEnd(event.getJavaHandlerPackage().replace(".", File.separator)) + event.getJavaHandlerName() + ".java");
            boolean cover = false;
            if (file.exists()) {
                if (!javaConfig.isEventHandlerOverwrite()) {
                    logger.warn("eventHandler 存在不能生成 {} {}", file.getName(), file.getAbsoluteFile());
                    continue;
                } else {
                    cover = true;
                }

            } else {
                checkFile(file);
            }
            event.setSovereignty(Sovereignty.getInstance().sovereigntyJavaComment());
            if (cover) {
                logger.info("覆盖生成 eventHandler {} {}", file.getName(), file.getAbsoluteFile());
            } else {
                logger.info("生成 eventHandler {} {}", file.getName(), file.getAbsoluteFile());
            }
            Generator.generate(event, template, file);
        }

    }


    @Override
    public String getTemplateDir() {
        return TEMPLATE_DIR;
    }
}
