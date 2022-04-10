package com.senpure.io.generator.util;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;


public class LuaNameStyle implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleScalar simpleScalar= (SimpleScalar) list.get(0);
        return MessageUtil.luaNameStyle(simpleScalar.getAsString());
    }
}
