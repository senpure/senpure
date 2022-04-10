package com.senpure.io.generator.executor;

import com.senpure.base.util.Assert;
import com.senpure.base.util.StringUtil;
import com.senpure.io.generator.habit.ScriptLanguageConfig;
import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.*;
import com.senpure.io.generator.util.LowerCamelCaseNameRule;
import com.senpure.template.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * ScriptLanguageExecutor
 *
 * @author senpure
 * @time 2019-09-25 14:49:32
 */
public abstract class ScriptLanguageExecutor<T extends ScriptLanguageConfig> extends AbstractLanguageExecutor<T> {

    protected RequireBean requireBean = new RequireBean();
    protected Configuration cfg;
    protected ExecutorContext context;

    public abstract Language getLanguage(Bean bean);

    public abstract String getProtocolSuffix();

    public abstract MixBean getNewMixBean();

    public void execute(Configuration cfg, ExecutorContext context, ScriptLanguageConfig config) {
        this.cfg = cfg;
        this.context = context;
        requireBean.getFileNames().clear();
        switch (config.getType()) {
            case ScriptLanguageConfig.TYPE_MIX:
                generateByMix(config);
                break;
            case ScriptLanguageConfig.TYPE_FILE:
                generateByFile(config);
                break;
            case ScriptLanguageConfig.TYPE_NAMESPACE:
                generateByNamespace(config);
                break;
            default:
                generate(config);
                break;
        }
        if (config.isGenerateRequire() && requireBean.getFileNames().size() > 0) {
            generateRequire( config);
        }
    }



    private void generate(ScriptLanguageConfig config) {
        generateByFile(config);
    }

    private void generateByMix(ScriptLanguageConfig config) {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getProtocolTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        MixBean bean = getNewMixBean();
        bean.setBeans(context.getBeans());
        bean.setEnums(context.getEnums());
        bean.setMessages(context.getMessages());
        Comparator<Bean> nameShort = (x, y) -> {
            Language xL = getLanguage(x);
            Language yL = getLanguage(y);
            return (xL.getNamespace() + "." + xL.getName()).compareTo(yL.getNamespace() + "." + yL.getName());
        };
        Collections.sort(bean.getEnums(), nameShort);
        Collections.sort(bean.getBeans(), nameShort);
        Collections.sort(bean.getMessages(), (x, y) -> {
            Language xL = getLanguage(x);
            Language yL = getLanguage(y);
            int result = xL.getNamespace().compareTo(yL.getNamespace());
            if (result == 0) {
                return Integer.compare(x.getId(), y.getId());
            }
            return result;
        });
        bean.compute();

        File file;
        if (config.isAppendNamespace()) {
            requireBean.getFileNames().add(config.getMixFileName() + "." + config.getMixFileName() + getProtocolSuffix());
            file = new File(config.getProtocolCodeRootPath(), config.getMixFileName() +
                    File.separator + config.getMixFileName() + getProtocolSuffix());
        } else {
            requireBean.getFileNames().add(config.getMixFileName() + getProtocolSuffix());
            file = new File(config.getProtocolCodeRootPath(), config.getMixFileName() + getProtocolSuffix());
        }
        generateByMix(bean, template, file);

    }

    public void generateByMix(MixBean bean, Template template, File file) {
        generate(bean, template, file, true);
    }

    private void generateByFile(ScriptLanguageConfig config) {
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
                requireBean.getFileNames().add(temp + "." + fileName + getProtocolSuffix());
                file = new File(config.getProtocolCodeRootPath(),
                        FileUtil.fullFileEnd(temp
                                .replace(".", File.separator)) +
                                fileName + getProtocolSuffix());
            } else {
                requireBean.getFileNames().add(fileName + getProtocolSuffix());
                file = new File(config.getProtocolCodeRootPath(), fileName + getProtocolSuffix());
            }
            MixBean bean = getNewMixBean();

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
            MixBean bean = entry.getValue();
            bean.compute();
            generateByFile(bean, template, entry.getKey());

        }
    }

    public void generateByFile(MixBean bean, Template template, File file) {
        generate(bean, template, file, true);
    }

    private void generateByNamespace(ScriptLanguageConfig config) {
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
                requireBean.getFileNames().add(namespace + "." + name + getProtocolSuffix());
                file = new File(config.getProtocolCodeRootPath(),
                        FileUtil.fullFileEnd(namespace.replace(".", File.separator)) + name + getProtocolSuffix());

            } else {
                requireBean.getFileNames().add(entry.getKey() + getProtocolSuffix());
                file = new File(config.getProtocolCodeRootPath(), entry.getKey().replace(".", File.separator) + getProtocolSuffix());
            }
            MixBean bean = getNewMixBean();
            List<Bean> beans = entry.getValue();
            for (Bean b : beans) {
                pick(bean, b);
            }
            bean.compute();
            generateByNamespace(bean, template, file, entry.getKey());

        }
    }

    public void generateByNamespace(MixBean bean, Template template, File file, String namespace) {
        generate(bean, template, file, true);
    }

    protected void dispatchByNamespace(Map<String, List<Bean>> namespaceMap, List<? extends Bean> beans) {
        for (Bean bean : beans) {
            Language language = getLanguage(bean);
            List<Bean> list = namespaceMap.get(language.getNamespace());
            if (list == null) {
                list = new ArrayList<>();
                namespaceMap.put(language.getNamespace(), list);
            }
            list.add(bean);
        }
    }

    public void generateRequire(ScriptLanguageConfig config) {
        Template template = null;
        try {
            template = cfg.getTemplate(config.getRequireTemplate(), "utf-8");
        } catch (IOException e) {
            Assert.error(e);
        }
        File file = new File(config.getProtocolCodeRootPath(), "require" + getProtocolSuffix());
        generate(requireBean, template, file, config.isRequireOverwrite());
    }


}
