package com.senpure.javafx;

import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.core.io.ResourceLoader;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Javafx
 *
 * @author senpure
 * @time 2020-06-18 16:32:07
 */
public class Javafx {

    private static Stage primaryStage;

    private static SystemTray systemTray;

    private static HostServices hostServices;

    private static JavafxProperties javafxProperties;


    private static ResourceLoader resourceLoader;

    private static SceneFactory sceneFactory = new SceneFactory() {
    };

    // private static final Logger logger = LoggerFactory.getLogger(Javafx.class);


    private static final Map<Class<?>, JavafxViewWrap> wrapMap = new HashMap<>();

    public static void showView(Class<? extends JavafxView> view) {

        showView(getView(view));

    }
    public static void showView(JavafxView view) {

       showView(view.getClass());

    }

    private  static void showView(JavafxViewWrap view) {
        stageSetView(primaryStage, view);
        primaryStage.show();

    }
    public static void showView(Stage stage, JavafxView view) {

        stageSetView(stage, getView(view.getClass()));

        stage.showAndWait();
    }


    private static void stageSetView(Stage stage, JavafxViewWrap view) {
        if (stage.getScene() == null) {
            if (stage.getTitle() == null) {
                String title;
                if (stage == primaryStage) {
                    title = view.getOriginalTitle();
                    if (title.isEmpty()) {
                        title = javafxProperties.getTitle();
                        if (title == null || title.isEmpty()) {
                            title = view.getTitle();
                        }
                    }
                } else {
                    title = view.getTitle();
                }

                if (title.startsWith("%")) {
                    ResourceBundle resourceBundle = view.getResourceBundle();
                    title = resourceBundle.getString(title.substring(1));
                }
                stage.setTitle(title);
            }
            Scene scene = sceneFactory.get(view.getView());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(view.getView());
        }
    }

    private  static JavafxViewWrap getView(Class<? extends JavafxView> view) {

        JavafxViewWrap javafxViewWrap = wrapMap.get(view);

        if (javafxViewWrap == null) {
            javafxViewWrap = new JavafxViewWrap(view);
            wrapMap.put(view, javafxViewWrap);
            return wrapMap.get(view);
        }
        return javafxViewWrap;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Javafx.primaryStage = primaryStage;
    }

    public static SystemTray getSystemTray() {
        return systemTray;
    }

    public static void setSystemTray(SystemTray systemTray) {
        Javafx.systemTray = systemTray;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }

    public static void setHostServices(HostServices hostServices) {
        Javafx.hostServices = hostServices;
    }


    public static JavafxProperties getJavafxProperties() {
        return javafxProperties;
    }

    public static void setJavafxProperties(JavafxProperties javafxProperties) {
        Javafx.javafxProperties = javafxProperties;
    }

    public static ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public static void setResourceLoader(ResourceLoader resourceLoader) {
        Javafx.resourceLoader = resourceLoader;
    }

    public static SceneFactory getSceneFactory() {
        return sceneFactory;
    }

    public static void setSceneFactory(SceneFactory sceneFactory) {
        Javafx.sceneFactory = sceneFactory;
    }
}
