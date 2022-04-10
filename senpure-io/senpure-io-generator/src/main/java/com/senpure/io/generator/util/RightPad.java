package com.senpure.io.generator.util;

import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class RightPad implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
       // System.out.println(list);
        SimpleScalar simpleScalar= (SimpleScalar) list.get(0);
        SimpleNumber simpleNumber= (SimpleNumber) list.get(1);
        String str = simpleScalar.getAsString();
        int len =  simpleNumber.getAsNumber().intValue();
        return StringUtils.rightPad(str,len);
    }
}
