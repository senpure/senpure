package com.senpure.io.generator.util;


import com.senpure.base.AppEvn;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class TemplateUtil {
    private static final Logger logger
            = LoggerFactory.getLogger(TemplateUtil.class);
    private static File templateDir;

    public static File templateDir() {
        if (templateDir != null) {
            return templateDir;
        }
        String root = AppEvn.getClassRootPath();

        templateDir = new File(root, "template");
        logger.debug("模板文件路径{}",templateDir.getAbsolutePath());
        return templateDir;
    }

    public static void share(Configuration cfg) {
        cfg.setSharedVariable("rightPad", new RightPad());
        cfg.setSharedVariable("var32Size", new Var32Size());
        cfg.setSharedVariable("luaNameStyle", new LuaNameStyle());
        cfg.setSharedVariable("lowerCamelCase", new LowerCamelCaseNameRule());
//        try {
//            cfg.setSharedVariable("luaImplPrefix", Constant.LUA_IMPL_SC_PREFIX);
//        } catch (TemplateModelException e) {
//            e.printStackTrace();
//        }
    }
    public static void main(String[] args) {


    }
}
