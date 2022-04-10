package com.senpure.io.generator.habit;

/**
 * ScriptLanguageConfig
 *
 * @author senpure
 * @time 2019-09-23 11:43:44
 */
public abstract class ScriptLanguageConfig extends AbstractLanguageConfig {
    public final static String TYPE_MIX = "MIX";
    public final static String TYPE_FILE = "FILE";
    public final static String TYPE_NAMESPACE = "NAMESPACE";


    public ScriptLanguageConfig() {
        setScMessageHandlerTemplate("scMessageHandler.ftl");
    }

    private String type = TYPE_MIX;
    /**
     * 合并
     */
    private String mixFileName = "protocol";


    private String protocolTemplate = "protocol.ftl";
    private String requireTemplate = "require.ftl";

    private boolean appendNamespace = false;

    private boolean generateRequire = true;
    private boolean generateProtocol = true;


    private boolean requireOverwrite = true;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMixFileName() {
        return mixFileName;
    }

    public void setMixFileName(String mixFileName) {
        this.mixFileName = mixFileName;
    }


    public String getProtocolTemplate() {
        return protocolTemplate;
    }

    public void setProtocolTemplate(String protocolTemplate) {
        this.protocolTemplate = protocolTemplate;
    }

    public String getRequireTemplate() {
        return requireTemplate;
    }

    public void setRequireTemplate(String requireTemplate) {
        this.requireTemplate = requireTemplate;
    }

    public boolean isAppendNamespace() {
        return appendNamespace;
    }

    public void setAppendNamespace(boolean appendNamespace) {
        this.appendNamespace = appendNamespace;
    }

    public boolean isGenerateRequire() {
        return generateRequire;
    }

    public void setGenerateRequire(boolean generateRequire) {
        this.generateRequire = generateRequire;
    }

    public boolean isGenerateProtocol() {
        return generateProtocol;
    }

    public void setGenerateProtocol(boolean generateProtocol) {
        this.generateProtocol = generateProtocol;
    }

    public boolean isRequireOverwrite() {
        return requireOverwrite;
    }

    public void setRequireOverwrite(boolean requireOverwrite) {
        this.requireOverwrite = requireOverwrite;
    }
}
