package com.senpure.io.generator.habit;

import com.senpure.io.generator.executor.JavaExecutor;
import com.senpure.io.generator.executor.LanguageExecutor;

/**
 * JavaConfig
 *
 * @author senpure
 * @time 2019-07-09 17:00:01
 */
public class JavaConfig  extends AbstractLanguageConfig{

    @Override
    public LanguageExecutor languageExecutor() {
        return new JavaExecutor();
    }
}
