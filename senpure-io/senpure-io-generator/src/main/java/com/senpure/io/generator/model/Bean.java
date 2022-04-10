package com.senpure.io.generator.model;

import com.senpure.io.generator.Constant;
import com.senpure.template.sovereignty.TemplateBean;

import java.util.*;


public class Bean extends TemplateBean {


    private Location nameLocation = new Location();
    //命名空间独立于语言,同一个命名空间name必须唯一
    private String namespace;
    //原始字符
    private String originalName;
    //原始字符首字母大写
    private String name;

    //所在的java包
    private String javaPackage;


    private List<Field> fields = new ArrayList<>();
    private boolean hasExplain;
    private String explain;
    //引用其他的去重
    private Map<String, Field> singleField = new HashMap<>();
    //是否包含其他bean
    private boolean hasBean = false;

    //字段的长度格式化toString使用
    private int fieldMaxLen = 0;
    private boolean generate = true;
    //协议文件全路径
    private String filePath;

    private  Lua lua;
    private JavaScript js;

    public void setExplain(String explain) {
        this.explain = explain;
        if (explain != null && explain.trim().length() > 0) {
            hasExplain = true;
        }
    }


    public boolean isHasNextIndent() {

        for (Field field : fields) {
            if (field.isList()) {
                return true;
            }
            if (!field.isBaseField()) {
                Bean bean = field.getBean();
                if (!(bean instanceof Enum)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isEnum() {
        return false;
    }

    public String getJavaName() {
        return getName();
    }

    public String getType() {
        return Constant.ENTITY_TYPE_BEAN;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public boolean isHasExplain() {
        return hasExplain;
    }

    public String getExplain() {
        return explain;
    }

    public Map<String, Field> getSingleField() {
        return singleField;
    }

    public void setSingleField(Map<String, Field> singleField) {
        this.singleField = singleField;
    }


    public boolean isHasBean() {
        return hasBean;
    }

    public void setHasBean(boolean hasBean) {
        this.hasBean = hasBean;
    }


    public int getFieldMaxLen() {
        return fieldMaxLen;
    }

    public void setFieldMaxLen(int fieldMaxLen) {
        this.fieldMaxLen = fieldMaxLen;
    }

    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }


    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }


    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Location getNameLocation() {
        return nameLocation;
    }

    public void setNameLocation(Location nameLocation) {
        this.nameLocation = nameLocation;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bean bean = (Bean) o;
        return Objects.equals(getNamespace(), bean.getNamespace()) &&
                Objects.equals(getType(), ((Bean) o).getType()) &&
                Objects.equals(getOriginalName(), bean.getOriginalName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNamespace(), getType(), getOriginalName());
    }

    @Override
    public String toString() {
        return "Bean{" +
                "type='" + getType() + '\'' +
                ",name='" + name + '\'' +
                '}';
    }

    public Lua getLua() {
        return lua;
    }

    public void setLua(Lua lua) {
        this.lua = lua;
    }

    public JavaScript getJs() {
        return js;
    }

    public void setJs(JavaScript js) {
        this.js = js;
    }
}
