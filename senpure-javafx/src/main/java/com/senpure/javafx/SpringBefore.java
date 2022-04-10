package com.senpure.javafx;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * SplashStage
 *
 * @author senpure
 * @time 2020-06-19 16:30:35
 */
public class SpringBefore {
    private static final String DEFAULT_IMAGE = "/splash/javafx.png";
    private static final String SPLASH_IMAGE = "/splash";
    private Stage stage;
    private Stage parentStage;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private boolean close;

    public void showSplash() {
        final ImageView imageView = new ImageView(getClass().getResource(getSplashImagePath()).toExternalForm());
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(22, 22);
        indicator.setLayoutX(imageView.getImage().getWidth() - 22);
        indicator.setLayoutY(imageView.getImage().getHeight() - 22);
        indicator.setStyle("-fx-progress-color:#ffffff");
        Pane pane = new Pane();
        pane.setPrefSize(imageView.getImage().getWidth(), imageView.getImage().getHeight());
        pane.getChildren().addAll(imageView, indicator);
        //final VBox vbox = new VBox();
        //  vbox.getChildren().addAll(imageView, indicator);
        parentStage = new Stage(StageStyle.UTILITY);
        parentStage.setOpacity(0);
        stage = new Stage(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(pane));
        stage.initOwner(parentStage);
        parentStage.setIconified(true);
        parentStage.show();
        stage.show();
//        if (!stage.isAlwaysOnTop()) {
//            stage.setAlwaysOnTop(true);
//            stage.setAlwaysOnTop(false);
//        }

    }

    public void closeSplash(boolean animation) {

        closeSplash(null, animation);
    }

    public void closeSplash(@Nullable Runnable runnable, boolean animation) {
        if (close) {
            return;
        }
        close = true;
        if (animation) {
            double height = stage.getHeight();
            double width = stage.getWidth();
            double X = stage.getX();
            double opacity = stage.getOpacity();
            Animation transition = new Transition() {
                {
                    setCycleDuration(Duration.millis(300));
                    setInterpolator(Interpolator.EASE_IN);
                }

                @Override
                protected void interpolate(double frac) {
                    stage.setHeight(height);
                    double x = width * frac;
                    stage.setWidth(width - x);
                    stage.setX(X + x);
                    stage.setOpacity(opacity - opacity * frac);
                }
            };

            transition.setOnFinished(event -> {
                closeSplash0(runnable);
            });

            transition.play();
        } else {
            closeSplash0(runnable);
        }

    }

    private void closeSplash0(@Nullable Runnable runnable) {
        parentStage.hide();
        stage.hide();
        parentStage.close();
        stage.close();
        if (runnable != null) {
            runnable.run();
        }
    }

    public boolean isSplash() {
        return true;
    }


    public String getSplashImagePath() {
        List<String> suffixList = Arrays.asList(".png", ".jpg");
        for (String suffix : suffixList) {
            String imagePath = SPLASH_IMAGE + suffix;
            URL url = getClass().getResource(imagePath);
            if (url != null) {
                logger.debug("splash imagePath {}", imagePath);
                return imagePath;
            }
        }
        return DEFAULT_IMAGE;
    }

    public boolean isInstallAnsiConsole() {

        return true;
    }


}
