package com.senpure.io.generator.executor;

import freemarker.template.Configuration;

/**
 * LanguageExecutor
 *
 * @author senpure
 * @time 2019-09-23 11:16:19
 */
public interface LanguageExecutor<T> {

    void generate(Configuration cfg, ExecutorContext context, T config);

    String getTemplateDir();
}
