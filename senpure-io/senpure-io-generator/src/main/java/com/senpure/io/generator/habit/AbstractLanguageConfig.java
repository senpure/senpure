package com.senpure.io.generator.habit;

import com.senpure.base.AppEvn;

import java.io.File;

/**
 * AbstractLanguageConfig
 *
 * @author senpure
 * @time 2019-09-25 14:32:32
 */
public abstract class AbstractLanguageConfig implements LanguageConfig {
    private String protocolCodeRootPath;
    private String protocolCodeRootChooserPath;
    private String csMessageHandlerCodeRootPath;
    private String csMessageHandlerCodeRootChooserPath;
    private String scMessageHandlerCodeRootPath;
    private String scMessageHandlerCodeRootChooserPath;
    private String eventHandlerCodeRootPath;
    private String eventHandlerCodeRootChooserPath;
    private String enumTemplate = "enum.ftl";
    private String beanTemplate = "bean.ftl";
    private String messageTemplate = "message.ftl";
    private String eventTemplate = "event.ftl";
    private String csMessageHandlerTemplate = "messageHandler.ftl";
    private String scMessageHandlerTemplate = "consumerMessageHandler.ftl";
    private String eventHandlerTemplate = "eventHandler.ftl";
    private boolean generateEnum = true;
    private boolean generateBean = true;
    private boolean generateEvent = true;
    private boolean generateMessage = true;
    private boolean generateEventHandler = true;
    private boolean generateCSMessageHandler = true;
    private boolean generateSCMessageHandler = false;
    private boolean csMessageHandlerOverwrite = false;
    private boolean scMessageHandlerOverwrite = false;
    private boolean eventHandlerOverwrite = false;


    @Override
    public boolean hasSensitive() {
        if (csMessageHandlerOverwrite | scMessageHandlerOverwrite | eventHandlerOverwrite) {
            return true;
        }
        return false;
    }

    @Override
    public void notAllowSensitive() {
        csMessageHandlerOverwrite = false;
        scMessageHandlerOverwrite = false;
        eventHandlerOverwrite = false;
    }

    @Override
    public void initValue() {
        setEventHandlerCodeRootPath(AppEvn.getClassRootPath());
        setEventHandlerCodeRootChooserPath(new File(getEventHandlerCodeRootPath()).getParent());
        setProtocolCodeRootPath(AppEvn.getClassRootPath());
        setProtocolCodeRootChooserPath(new File(getProtocolCodeRootPath()).getParent());
        setCsMessageHandlerCodeRootPath(AppEvn.getClassRootPath());
        setCsMessageHandlerCodeRootChooserPath(new File(getCsMessageHandlerCodeRootPath()).getParent());
        setScMessageHandlerCodeRootPath(AppEvn.getClassRootPath());
        setScMessageHandlerCodeRootChooserPath(new File(getScMessageHandlerCodeRootPath()).getParent());
    }

    @Override
    public void checkSelf() {
       
        if (!new File(getEventHandlerCodeRootPath()).exists()) {
            setEventHandlerCodeRootChooserPath(new File(getEventHandlerCodeRootPath()).getParent());
        }
        if (!new File(getProtocolCodeRootPath()).exists()) {
            setProtocolCodeRootChooserPath(new File(getProtocolCodeRootPath()).getParent());
        }
        if (!new File(getCsMessageHandlerCodeRootPath()).exists()) {
            setCsMessageHandlerCodeRootChooserPath(new File(getCsMessageHandlerCodeRootPath()).getParent());
        }
        if (!new File(getScMessageHandlerCodeRootPath()).exists()) {
            setScMessageHandlerCodeRootChooserPath(new File(getScMessageHandlerCodeRootPath()).getParent());
        }

    }

    public String getProtocolCodeRootPath() {
        return protocolCodeRootPath;
    }

    public void setProtocolCodeRootPath(String protocolCodeRootPath) {
        this.protocolCodeRootPath = protocolCodeRootPath;
    }

    public String getProtocolCodeRootChooserPath() {
        return protocolCodeRootChooserPath;
    }

    public void setProtocolCodeRootChooserPath(String protocolCodeRootChooserPath) {
        this.protocolCodeRootChooserPath = protocolCodeRootChooserPath;
    }

    public String getCsMessageHandlerCodeRootPath() {
        return csMessageHandlerCodeRootPath;
    }

    public void setCsMessageHandlerCodeRootPath(String csMessageHandlerCodeRootPath) {
        this.csMessageHandlerCodeRootPath = csMessageHandlerCodeRootPath;
    }

    public String getCsMessageHandlerCodeRootChooserPath() {
        return csMessageHandlerCodeRootChooserPath;
    }

    public void setCsMessageHandlerCodeRootChooserPath(String csMessageHandlerCodeRootChooserPath) {
        this.csMessageHandlerCodeRootChooserPath = csMessageHandlerCodeRootChooserPath;
    }

    public String getScMessageHandlerCodeRootPath() {
        return scMessageHandlerCodeRootPath;
    }

    public void setScMessageHandlerCodeRootPath(String scMessageHandlerCodeRootPath) {
        this.scMessageHandlerCodeRootPath = scMessageHandlerCodeRootPath;
    }

    public String getScMessageHandlerCodeRootChooserPath() {
        return scMessageHandlerCodeRootChooserPath;
    }

    public void setScMessageHandlerCodeRootChooserPath(String scMessageHandlerCodeRootChooserPath) {
        this.scMessageHandlerCodeRootChooserPath = scMessageHandlerCodeRootChooserPath;
    }

    public String getEventHandlerCodeRootPath() {
        return eventHandlerCodeRootPath;
    }

    public void setEventHandlerCodeRootPath(String eventHandlerCodeRootPath) {
        this.eventHandlerCodeRootPath = eventHandlerCodeRootPath;
    }

    public String getEventHandlerCodeRootChooserPath() {
        return eventHandlerCodeRootChooserPath;
    }

    public void setEventHandlerCodeRootChooserPath(String eventHandlerCodeRootChooserPath) {
        this.eventHandlerCodeRootChooserPath = eventHandlerCodeRootChooserPath;
    }

    public String getEnumTemplate() {
        return enumTemplate;
    }

    public void setEnumTemplate(String enumTemplate) {
        this.enumTemplate = enumTemplate;
    }

    public String getBeanTemplate() {
        return beanTemplate;
    }

    public void setBeanTemplate(String beanTemplate) {
        this.beanTemplate = beanTemplate;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getEventTemplate() {
        return eventTemplate;
    }

    public void setEventTemplate(String eventTemplate) {
        this.eventTemplate = eventTemplate;
    }

    public String getCsMessageHandlerTemplate() {
        return csMessageHandlerTemplate;
    }

    public void setCsMessageHandlerTemplate(String csMessageHandlerTemplate) {
        this.csMessageHandlerTemplate = csMessageHandlerTemplate;
    }

    public String getScMessageHandlerTemplate() {
        return scMessageHandlerTemplate;
    }

    public void setScMessageHandlerTemplate(String scMessageHandlerTemplate) {
        this.scMessageHandlerTemplate = scMessageHandlerTemplate;
    }

    public String getEventHandlerTemplate() {
        return eventHandlerTemplate;
    }

    public void setEventHandlerTemplate(String eventHandlerTemplate) {
        this.eventHandlerTemplate = eventHandlerTemplate;
    }

    public boolean isGenerateEnum() {
        return generateEnum;
    }

    public void setGenerateEnum(boolean generateEnum) {
        this.generateEnum = generateEnum;
    }

    public boolean isGenerateBean() {
        return generateBean;
    }

    public void setGenerateBean(boolean generateBean) {
        this.generateBean = generateBean;
    }

    public boolean isGenerateEvent() {
        return generateEvent;
    }

    public void setGenerateEvent(boolean generateEvent) {
        this.generateEvent = generateEvent;
    }

    public boolean isGenerateMessage() {
        return generateMessage;
    }

    public void setGenerateMessage(boolean generateMessage) {
        this.generateMessage = generateMessage;
    }

    public boolean isGenerateEventHandler() {
        return generateEventHandler;
    }

    public void setGenerateEventHandler(boolean generateEventHandler) {
        this.generateEventHandler = generateEventHandler;
    }

    public boolean isGenerateCSMessageHandler() {
        return generateCSMessageHandler;
    }

    public void setGenerateCSMessageHandler(boolean generateCSMessageHandler) {
        this.generateCSMessageHandler = generateCSMessageHandler;
    }

    public boolean isGenerateSCMessageHandler() {
        return generateSCMessageHandler;
    }

    public void setGenerateSCMessageHandler(boolean generateSCMessageHandler) {
        this.generateSCMessageHandler = generateSCMessageHandler;
    }

    public boolean isCsMessageHandlerOverwrite() {
        return csMessageHandlerOverwrite;
    }

    public void setCsMessageHandlerOverwrite(boolean csMessageHandlerOverwrite) {
        this.csMessageHandlerOverwrite = csMessageHandlerOverwrite;
    }

    public boolean isScMessageHandlerOverwrite() {
        return scMessageHandlerOverwrite;
    }

    public void setScMessageHandlerOverwrite(boolean scMessageHandlerOverwrite) {
        this.scMessageHandlerOverwrite = scMessageHandlerOverwrite;
    }

    public boolean isEventHandlerOverwrite() {
        return eventHandlerOverwrite;
    }

    public void setEventHandlerOverwrite(boolean eventHandlerOverwrite) {
        this.eventHandlerOverwrite = eventHandlerOverwrite;
    }
}
