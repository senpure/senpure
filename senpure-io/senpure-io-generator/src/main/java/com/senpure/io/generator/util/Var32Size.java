package com.senpure.io.generator.util;

import com.senpure.io.protocol.CompressBean;
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;


public class Var32Size implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleNumber simpleNumber = (SimpleNumber) list.get(0);
        return computeVar32Size(simpleNumber.getAsNumber().intValue());
    }

    public static int computeVar32Size(int value) {
        return CompressBean.computeVar32Size(value);
    }
}
