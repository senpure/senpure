package com.senpure.io.generator.model;

import com.senpure.io.generator.Constant;

/**
 * Enum
 *
 * @author senpure
 * @time 2019-05-17 11:21:25
 */
public class Enum extends Bean {

    // 枚举时使用
    private Field defaultField;

    public boolean isEnum() {
        return true;
    }
    public String getJavaName() {
        return getName();
    }
    public String getType() {
        return Constant.ENTITY_TYPE_ENUM;
    }

    public Field getDefaultField() {
        return defaultField;
    }


    public void setDefaultField(Field defaultField) {
        this.defaultField = defaultField;
    }
}
