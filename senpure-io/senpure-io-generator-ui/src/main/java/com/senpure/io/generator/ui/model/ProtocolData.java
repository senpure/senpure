package com.senpure.io.generator.ui.model;

import com.senpure.io.generator.model.Bean;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * ProtocolData
 *
 * @author senpure
 * @time 2019-07-08 16:55:35
 */
public class ProtocolData {

    private Bean bean;
    private StringProperty name;
    private BooleanProperty generate;
    private StringProperty type;
    private StringProperty explain;

    public ProtocolData(Bean bean,String beanType) {
        this.bean = bean;
        name = new SimpleStringProperty(bean.getName());
        generate = new SimpleBooleanProperty(true);
        explain = new SimpleStringProperty(bean.getExplain());
        type = new SimpleStringProperty(beanType);
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isGenerate() {
        return generate.get();
    }

    public BooleanProperty generateProperty() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate.set(generate);
        bean.setGenerate(generate);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getExplain() {
        return explain.get();
    }

    public StringProperty explainProperty() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain.set(explain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtocolData that = (ProtocolData) o;
        return getBean().equals(that.getBean());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBean());
    }
}
