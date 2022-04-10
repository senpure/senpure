package com.senpure.io.generator.executor;


import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import com.senpure.io.generator.habit.JavaScriptConfig;
import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.*;
import com.senpure.io.generator.util.LowerCamelCaseNameRule;
import com.senpure.template.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JavaScriptExecutor extends AbstractLanguageExecutor<JavaScriptConfig> {

    public static final String TEMPLATE_DIR = "js";
    protected RequireBean requireBean = new RequireBean();
    protected Configuration cfg;
    protected ExecutorContext context;
    protected JavaScriptConfig config;

    @Override
    public String getTemplateDir() {
        return TEMPLATE_DIR;
    }

    @Override
    public void generate(Configuration cfg, ExecutorContext context, JavaScriptConfig config) {
        this.cfg = cfg;
        this.context = context;
        this.config = config;
        requireBean.getFileNames().clear();
        switch (config.getType()) {
            case JavaScriptConfig.TYPE_MIX:
                generateByMix();
                break;
            case JavaScriptConfig.TYPE_FILE:
                generateByFile();
                break;
            case JavaScriptConfig.TYPE_NAMESPACE:
                generateByNamespace();
                break;
            default:
                generate();
                break;
        }
        if (config.isGenerateRequire() && requireBean.getFileNames().size() > 0) {
            generateRequire();
        }


    }

    private void generate() {
        generateByFile();
    }

    private void generateByMix() {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getProtocolTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        MixBean bean = new JsMixBean();
        bean.setBeans(context.getBeans());
        bean.setEnums(context.getEnums());
        bean.setMessages(context.getMessages());
        NameShort nameShort = new NameShort();
        Collections.sort(bean.getEnums(), nameShort);
        Collections.sort(bean.getBeans(), nameShort);
        Collections.sort(bean.getMessages(), (x, y) -> {
            int result = x.getJs().getNamespace().compareTo(y.getJs().getNamespace());
            if (result == 0) {
                return Integer.compare(x.getId(), y.getId());
            }
            return result;
        });
        bean.compute();

        File file;
        if (config.isAppendNamespace()) {
            requireBean.getFileNames().add(config.getMixFileName() + "/" + config.getMixFileName() + ".js");
            file = new File(config.getProtocolCodeRootPath(), config.getMixFileName() +
                    File.separator + config.getMixFileName() + ".js");
        } else {
            requireBean.getFileNames().add(config.getMixFileName() + ".js");
            file = new File(config.getProtocolCodeRootPath(), config.getMixFileName() + ".js");
        }
        generate(bean, template, file, true);
        if (config.isGenerateDts()) {
            generateDts(bean, config.getMixFileName());
        }
    }

    private void generateByFile() {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getProtocolTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        Map<String, List<Bean>> fileMap = new HashMap<>();
        dispatchByFile(fileMap, context.getBeans());
        dispatchByFile(fileMap, context.getEnums());
        dispatchByFile(fileMap, context.getMessages());
        Map<File, MixBean> MixBeanMap = new HashMap<>();
        for (Map.Entry<String, List<Bean>> entry : fileMap.entrySet()) {
            List<Bean> beans = entry.getValue();
            if (beans.size() == 0) {
                continue;
            }
            String fileName = new File(entry.getKey()).getName();
            fileName = fileName.substring(0, fileName.length() - 3);
            File file;
            if (config.isAppendNamespace()) {
                String temp = LowerCamelCaseNameRule.nameRule(beans.get(0).getJs().getNamespace());
                requireBean.getFileNames().add(temp + "/" + fileName + ".");
                file = new File(config.getProtocolCodeRootPath(),
                        FileUtil.fullFileEnd(temp
                                .replace(".", File.separator)) +
                                fileName + ".");
            } else {
                requireBean.getFileNames().add(fileName + ".");
                file = new File(config.getProtocolCodeRootPath(), fileName + ".");
            }
            MixBean bean = new JsMixBean();

            for (Bean b : beans) {
                if (b instanceof Message) {
                    bean.getMessages().add((Message) b);
                } else if (b instanceof com.senpure.io.generator.model.Enum) {
                    bean.getEnums().add((Enum) b);
                } else {
                    bean.getBeans().add(b);
                }
            }
            MixBean MixBean = MixBeanMap.get(file);
            if (MixBean == null) {
                MixBeanMap.put(file, bean);
            } else {
                MixBean.merge(bean);
            }
        }
        for (Map.Entry<File, MixBean> entry : MixBeanMap.entrySet()) {
            MixBean MixBean = entry.getValue();
            MixBean.compute();
            generate(MixBean, template, entry.getKey(), true);
            if (config.isGenerateDts()) {
                generateDts(MixBean, config.getMixFileName());
            }
        }
    }

    private void generateByNamespace() {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getProtocolTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        Map<String, List<Bean>> namespaceMap = new HashMap<>();
        dispatchByNamespace(namespaceMap, context.getBeans());
        dispatchByNamespace(namespaceMap, context.getEnums());
        dispatchByNamespace(namespaceMap, context.getMessages());

        for (Map.Entry<String, List<Bean>> entry : namespaceMap.entrySet()) {
            File file;
            if (config.isAppendNamespace()) {
                String namespace = LowerCamelCaseNameRule.nameRule(entry.getKey());
                String name = namespace;
                int index = StringUtil.indexOf(namespace, ".", 1, true);
                if (index > -1) {
                    name = namespace.substring(index + 1);
                }
                requireBean.getFileNames().add(namespace + "/" + name + ".");
                file = new File(config.getProtocolCodeRootPath(), FileUtil.fullFileEnd(namespace.replace(".", File.separator)) + name + ".");

            } else {
                requireBean.getFileNames().add(entry.getKey() + ".");
                file = new File(config.getProtocolCodeRootPath(), entry.getKey().replace(".", File.separator) + ".");
            }
            MixBean bean = new JsMixBean();
            List<Bean> beans = entry.getValue();
            for (Bean b : beans) {
                pick(bean, b);
            }
            bean.compute();
            generate(bean, template, file, true);
        }
    }

    protected void dispatchByNamespace(Map<String, List<Bean>> namespaceMap, List<? extends Bean> beans) {
        for (Bean bean : beans) {
            List<Bean> list = namespaceMap.get(bean.getJs().getNamespace());
            if (list == null) {
                list = new ArrayList<>();
                namespaceMap.put(bean.getJs().getNamespace(), list);
            }
            list.add(bean);
        }
    }

    private void generateRequire() {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getRequireTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        File file = new File(config.getProtocolCodeRootPath(), "require.js");
        generate(requireBean, template, file, config.isRequireOverwrite());
    }

    private void generateDts(MixBean mixBean, String fileName) {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getDtsTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        File file = new File(config.getDtsCodeRootPath(), fileName + ".d.ts");
        generate(mixBean, template, file, config.isRequireOverwrite());
    }

    private class NameShort implements Comparator<Bean> {

        @Override
        public int compare(Bean x, Bean y) {
            return (x.getJs().getNamespace() + "." + x.getJs().getName())
                    .compareTo((y.getJs().getNamespace() + "." + y.getJs().getName()));
        }
    }

    public static class JsMixBean extends MixBean {


        @Override
        public Language getLanguage(Bean bean) {
            return bean.getJs();
        }
    }
}
