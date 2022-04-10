package com.senpure.io.generator.habit;

import com.senpure.base.AppEvn;
import com.senpure.io.generator.executor.JavaScriptExecutor;
import com.senpure.io.generator.executor.LanguageExecutor;

import java.io.File;

/**
 * Config
 *
 * @author senpure
 * @time 2019-08-12 10:48:13
 */
public class JavaScriptConfig extends ScriptLanguageConfig {

    @Override
    public LanguageExecutor languageExecutor() {
        return new JavaScriptExecutor();
    }

    private String dtsTemplate = "dts.ftl";
    private boolean generateDts = true;
    private String dtsCodeRootPath;
    private String dtsCodeRootChooserPath;

    @Override
    public void initValue() {
        super.initValue();
        setDtsCodeRootPath(new File(AppEvn.getClassRootPath(),"@types").getAbsolutePath());
        setDtsCodeRootChooserPath(AppEvn.getClassRootPath());
    }

    public String getDtsTemplate() {
        return dtsTemplate;
    }

    public void setDtsTemplate(String dtsTemplate) {
        this.dtsTemplate = dtsTemplate;
    }

    public boolean isGenerateDts() {
        return generateDts;
    }

    public void setGenerateDts(boolean generateDts) {
        this.generateDts = generateDts;
    }

    public String getDtsCodeRootPath() {
        return dtsCodeRootPath;
    }

    public void setDtsCodeRootPath(String dtsCodeRootPath) {
        this.dtsCodeRootPath = dtsCodeRootPath;
    }

    public String getDtsCodeRootChooserPath() {
        return dtsCodeRootChooserPath;
    }

    public void setDtsCodeRootChooserPath(String dtsCodeRootChooserPath) {
        this.dtsCodeRootChooserPath = dtsCodeRootChooserPath;
    }
}
