package com.senpure.io.generator.executor;

import com.senpure.io.generator.model.Enum;
import com.senpure.io.generator.model.*;
import com.senpure.template.Generator;
import com.senpure.template.sovereignty.Sovereignty;
import com.senpure.template.sovereignty.TemplateBean;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AbstractLanguageExecutor
 *
 * @author senpure
 * @time 2019-09-23 11:27:47
 */
public abstract class AbstractLanguageExecutor<T> implements LanguageExecutor<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    protected void checkFile(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }


    protected void dispatchByFile(Map<String, List<Bean>> fileMap, List<? extends Bean> beans) {
        for (Bean bean : beans) {
            List<Bean> list = fileMap.get(bean.getFilePath());
            if (list == null) {
                list = new ArrayList<>();
                fileMap.put(bean.getFilePath(), list);
            }
            list.add(bean);
        }
    }



    protected void pick(MixBean bean, Bean b) {
        if (b instanceof Message) {
            bean.getMessages().add((Message) b);
        } else if (b instanceof Enum) {
            bean.getEnums().add((Enum) b);
        } else if (b instanceof Event) {
            bean.getEvents().add((Event) b);
        } else {
            bean.getBeans().add(b);
        }
    }
    protected void generate(TemplateBean bean, Template template, File file, boolean overwrite) {
        generate(bean,template,file,overwrite, Sovereignty.getInstance().sovereignty());
    }
    protected void generate(TemplateBean bean, Template template, File file, boolean overwrite, String sovereignty) {
        boolean thisOverwrite = false;
        if (file.exists()) {
            if (!overwrite) {
                logger.warn("文件存在不能生成 {} {}", file.getName(), file.getAbsoluteFile());
                return;
            } else {
                thisOverwrite = true;
            }

        } else {
            checkFile(file);
        }
        bean.setSovereignty(sovereignty);
        if (thisOverwrite) {
            logger.info("覆盖生成  {} {}", file.getName(), file.getAbsoluteFile());
        } else {
            logger.info("生成  {} {}", file.getName(), file.getAbsoluteFile());
        }
        Generator.generate(bean, template, file);
    }

}
