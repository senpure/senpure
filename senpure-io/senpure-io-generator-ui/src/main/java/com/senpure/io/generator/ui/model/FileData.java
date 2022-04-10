package com.senpure.io.generator.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;


public class FileData {

    private File file;
    private StringProperty name;

    private StringProperty path;


    public FileData(File file) {
        this.file = file;
        name = new SimpleStringProperty(file.getName());
        path = new SimpleStringProperty(file.getAbsolutePath());
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

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileData fileData = (FileData) o;

        return getPath() != null ? getPath().equals(fileData.getPath()) : fileData.getPath() == null;
    }

    @Override
    public int hashCode() {
        return getPath() != null ? getPath().hashCode() : 0;
    }
}
