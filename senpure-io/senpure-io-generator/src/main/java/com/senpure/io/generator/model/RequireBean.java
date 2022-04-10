package com.senpure.io.generator.model;

import com.senpure.template.sovereignty.TemplateBean;

import java.util.ArrayList;
import java.util.List;

/**
 * RequireBean
 *
 * @author senpure
 * @time 2019-09-23 11:51:01
 */
public class RequireBean extends TemplateBean {

    List<String> fileNames = new ArrayList<>();

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}