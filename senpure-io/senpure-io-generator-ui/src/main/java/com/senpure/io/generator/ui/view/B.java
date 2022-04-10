package com.senpure.io.generator.ui.view;

import com.senpure.base.util.Spring;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * B
 *
 * @author senpure
 * @time 2019-07-11 17:59:21
 */
public class B extends Application {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/i18n");
            // Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"),resourceBundle);

            primaryStage.setTitle("代码生成工具 senpure-generator");

            // primaryStage.setScene(new Scene(root, 300, 275));
            //  primaryStage.show();

            FXMLLoader loader = new FXMLLoader();

            //loader.setLocation(getClass().getClassLoader().getResource("/main.fxml"));
            loader.setLocation(getClass().getResource("animation.fxml"));
            loader.setResources(resourceBundle);

            //loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());

            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setFullScreen(false);

            primaryStage.setOnCloseRequest(event -> {
                logger.debug("close window");
                Spring.exit();


            });
            //Thread.currentThread().setUncaughtExceptionHandler((t, e) -> logger.error("出现错误", e));
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("出现错误", e));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {


            }));
        } catch (Exception e) {
            logger.error("出现错误", e);
        }


    }

}
