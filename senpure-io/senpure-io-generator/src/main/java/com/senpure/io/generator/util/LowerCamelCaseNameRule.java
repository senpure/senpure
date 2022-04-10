package com.senpure.io.generator.util;

import com.senpure.base.util.StringUtil;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * LowerCamelCaseNameRule
 *
 * @author senpure
 * @time 2019-07-29 11:43:11
 */
public class LowerCamelCaseNameRule implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        SimpleScalar simpleScalar= (SimpleScalar) list.get(0);
        return nameRule(simpleScalar.getAsString());
    }

    public static String nameRule(String name) {
        if (StringUtil.isUpperLetter(name.charAt(1))) {
            int len = name.length() - 1;
            int index = 0;
            for (int i = 1; i < len; i++) {
                if (!StringUtil.isUpperLetter(name.charAt(i + 1))) {
                    index = i - 1;
                    break;
                }
            }
            if (index > 0) {
                for (int i = 0; i <= index; i++) {
                    name = StringUtil.toLowerLetter(name, i);
                }
            }
            return name;

        }
        return StringUtil.toLowerLetter(name, 0);
    }
}
