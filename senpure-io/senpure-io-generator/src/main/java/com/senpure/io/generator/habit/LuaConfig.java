package com.senpure.io.generator.habit;

import com.senpure.io.generator.executor.LanguageExecutor;
import com.senpure.io.generator.executor.LuaExecutor;

/**
 * LuaConfig
 *
 * @author senpure
 * @time 2019-09-25 15:38:58
 */
public class LuaConfig extends ScriptLanguageConfig {
    @Override
    public LanguageExecutor languageExecutor() {
       return new LuaExecutor();
    }
}
