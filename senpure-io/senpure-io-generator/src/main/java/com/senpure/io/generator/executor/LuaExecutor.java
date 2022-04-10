package com.senpure.io.generator.executor;


import com.senpure.io.generator.habit.LuaConfig;
import com.senpure.io.generator.model.Bean;
import com.senpure.io.generator.model.Language;
import com.senpure.io.generator.model.MixBean;
import freemarker.template.Configuration;

public class LuaExecutor extends ScriptLanguageExecutor<LuaConfig> {

    public static final String TEMPLATE_DIR = "lua";

    @Override
    public Language getLanguage(Bean bean) {
        return bean.getLua();
    }

    @Override
    public String getProtocolSuffix() {
        return ".lua";
    }

    @Override
    public MixBean getNewMixBean() {
        return new LuaMixBean();
    }

    @Override
    public void generate(Configuration cfg, ExecutorContext context, LuaConfig config) {
        execute(cfg,context,config);
    }

    @Override
    public String getTemplateDir() {
        return TEMPLATE_DIR;
    }

    public static class LuaMixBean extends MixBean {
        @Override
        public Language getLanguage(Bean bean) {
            return bean.getLua();
        }
    }
}
