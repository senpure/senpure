package com.senpure.io.generator.ui.model;

import javafx.util.StringConverter;

import java.io.File;


public class FileConverter extends StringConverter<File> {

    @Override
    public String toString(File object) {
        return object.getName();
    }

    @Override
    public File fromString(String string) {
        return null;
    }
}
