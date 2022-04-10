package com.senpure.javafx;

import com.senpure.base.util.Spring;
import com.senpure.base.util.StringUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class JavafxViewWrap {
    private static final CachingMetadataReaderFactory READER_FACTORY = new CachingMetadataReaderFactory();


    private static List<String> globalCss;
    private static List<String> globalBaseNames;
    private static ResourceBundle emptyResourceBundle;

    private final Class<? extends JavafxView> view;
    private MergedAnnotation<View> annotation;


    private ResourceBundle resourceBundle;
    private FXMLLoader fxmlLoader;
    private Parent parent;
    private String title;
    private String originalTitle;

    private boolean warped;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public JavafxViewWrap(Class<? extends JavafxView> view) {
        this.view = view;
    }

    private void warp() {
        if (warped) {
            return;
        }

        warped = true;
        loadGlobalBasenames();
        loadGlobalCss();
        loadAnnotation();
        loadResourceBundle();
        loadFxmlLoader();

    }


    @NonNull
    public Parent getView() {
        if (parent != null) {
            return parent;
        }
        warp();
        Parent parent = fxmlLoader.getRoot();
        if (!globalCss.isEmpty()) {
            parent.getStylesheets().addAll(globalCss);
        }
        String[] css = annotation.getStringArray("css");
        if (css.length == 0) {
            String cssName = getConventionalName(".css");
            URL url = getClass().getResource(cssName);
            if (url != null) {
                parent.getStylesheets().add(url.toExternalForm());
            }

        } else {
            for (String s : css) {
                if (!s.endsWith(".css")) {
                    s += ".css";
                }
                URL url = getClass().getResource(s);
                if (url == null) {
                    Resource resource = Javafx.getResourceLoader().getResource(s);
                    if (resource.exists()) {
                        try {
                            parent.getStylesheets().add(resource.getURL().toExternalForm());
                        } catch (IOException e) {
                            logger.error("", e);
                        }
                    } else {
                        logger.warn("{} css文件定义错误,忽略该css", s);
                    }
                } else {
                    parent.getStylesheets().add(url.toExternalForm());
                }
            }
        }

        this.parent = parent;
        return parent;
    }

    @NonNull
    public FXMLLoader getFxmlLoader() {
        warp();
        return fxmlLoader;
    }

    @NonNull
    public ResourceBundle getResourceBundle() {
        warp();
        return resourceBundle;
    }

    @NonNull
    public String getTitle() {
        if (title != null) {
            return title;
        }
        warp();
        String temp = annotation.getString("title");
        if (temp.length() == 0) {
            temp = getConventionalName("");
        }
        title = temp;
        return title;
    }

    @NonNull
    public String getOriginalTitle() {
        if (originalTitle != null) {
            return originalTitle;
        }
        warp();
        originalTitle = annotation.getString("title");
        return originalTitle;
    }

    private void loadGlobalCss() {
        if (globalCss != null) {
            return;
        }
        List<String> tempCss = new ArrayList<>();
        JavafxProperties javafxProperties = Javafx.getJavafxProperties();
        if (StringUtils.hasText(javafxProperties.getCss())) {
            String[] css = StringUtils.commaDelimitedListToStringArray(javafxProperties.getCss());
            for (String s : css) {
                if (!s.endsWith(".css")) {
                    s += ".css";
                }
                Resource resource = Javafx.getResourceLoader().getResource(s.trim());
                if (resource.exists()) {
                    try {
                        tempCss.add(resource.getURL().toExternalForm());
                    } catch (IOException e) {
                        logger.error(s + " css文件定义错误,忽略该css", e);
                    }
                } else {
                    logger.warn("{} css文件定义错误,忽略该css", s);
                }

            }
        } else {
            Resource resource = Javafx.getResourceLoader().getResource(JavafxProperties.DEFAULT_CSS);
            if (resource.exists()) {
                try {
                    tempCss.add(resource.getURL().toExternalForm());
                } catch (IOException ignored) {
                }
            }
        }
        JavafxViewWrap.globalCss = tempCss;
    }

    private void loadGlobalBasenames() {
        if (globalBaseNames != null) {
            return;
        }
        List<String> tempBaseNames = new ArrayList<>();
        JavafxProperties javafxProperties = Javafx.getJavafxProperties();
        if (javafxProperties.getBasenames() != null) {
            tempBaseNames.addAll(javafxProperties.getBasenames());
        } else {
            String basename = JavafxProperties.DEFAULT_BASENAME;
            Resource resource = Javafx.getResourceLoader().getResource(basename + ".properties");
            if (resource.exists()) {
                tempBaseNames.add(basename);
            } else {
                resource = Javafx.getResourceLoader()
                        .getResource(basename + "_" + javafxProperties.getLocale().toString() + ".properties");
                if (resource.exists()) {
                    tempBaseNames.add(basename);
                }
            }
        }
        globalBaseNames = tempBaseNames;
    }

    private void loadAnnotation() {
        try {

            logger.debug("读取 {} View 注解数据", view);
            AnnotationMetadata annotationMetadata = READER_FACTORY.getMetadataReader(view.getName())
                    .getAnnotationMetadata();
            annotation = annotationMetadata.getAnnotations().get(View.class);
            if (!annotation.isPresent()) {
                logger.debug("{} 没有注解 默认使用空注解", view);
                annotation = READER_FACTORY.getMetadataReader(EmptyView.class.getName())
                        .getAnnotationMetadata()
                        .getAnnotations().get(View.class);
            }

        } catch (IOException e) {
            //不应该出现该错误
            logger.error("", e);
        }
    }

    private void loadResourceBundle() {
        ResourceBundle resourceBundle;
        String[] basenames = annotation.getStringArray("basenames");
        List<String> viewBasenames = new ArrayList<>(4);
        if (basenames.length > 0) {
            viewBasenames.addAll(Arrays.asList(basenames));
        }
        viewBasenames.addAll(globalBaseNames);

        if (viewBasenames.isEmpty()) {
            if (emptyResourceBundle == null) {
                resourceBundle = createResourceBundle(viewBasenames);
                emptyResourceBundle = resourceBundle;
            } else {
                resourceBundle = emptyResourceBundle;
            }
        } else {
            resourceBundle = createResourceBundle(viewBasenames);
        }
        this.resourceBundle = resourceBundle;
    }

    private void loadFxmlLoader() {
        JavafxProperties javafxProperties = Javafx.getJavafxProperties();
        String fxml = getFxmlName(annotation.getString("fxml"));
        logger.debug("{} fxml = {}", view.getName(), fxml);
        URL url = view.getResource(fxml);
        logger.debug("url {}", url);

        fxmlLoader = new FXMLLoader(url, resourceBundle);
        fxmlLoader.setControllerFactory(param -> {
            Object controller;
            try {
                controller = Spring.getBean(param);
            } catch (BeansException e) {
                Spring.regBean(param);
                controller = Spring.getBean(param);
            }
            return controller;
        });
        fxmlLoader.setCharset(javafxProperties.getEncoding());
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFxmlName(String name) {
        if (name.length() == 0) {

            return getConventionalName(".fxml");
        } else {
            if (!name.endsWith(".fxml")) {
                return name + ".fxml";
            }
            return name;
        }

    }

    private String getConventionalName(String suffix) {
        String modelName = view.getSimpleName();
        if (modelName.endsWith("Controller")) {
            modelName = modelName.substring(0, modelName.length() - 10);
        } else if (modelName.endsWith("View")) {
            modelName = modelName.substring(0, modelName.length() - 4);
        }

        return nameRule(modelName) + suffix;
    }

    private static String nameRule(String name) {
        if (name.length() > 1) {
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
        }
        return StringUtil.toLowerLetter(name, 0);
    }

    private ResourceBundle createResourceBundle(List<String> basenames) {
        JavafxProperties javafxProperties = Javafx.getJavafxProperties();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (!basenames.isEmpty()) {
            messageSource.setBasenames(basenames.toArray(new String[0]));
        }
        messageSource.setDefaultEncoding(javafxProperties.getEncoding().name());
        messageSource.setDefaultLocale(javafxProperties.getLocale());
        messageSource.setFallbackToSystemLocale(true);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setAlwaysUseMessageFormat(false);


        return new MessageSourceResourceBundle(messageSource,
                javafxProperties.getLocale());

    }

    @View
    private static class EmptyView {
    }

}
