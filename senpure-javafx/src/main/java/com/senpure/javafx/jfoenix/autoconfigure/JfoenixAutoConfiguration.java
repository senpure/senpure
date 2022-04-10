package com.senpure.javafx.jfoenix.autoconfigure;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import com.senpure.javafx.Javafx;
import com.senpure.javafx.SceneFactory;
import com.senpure.javafx.autoconfigure.JavafxAutoConfiguration;
import com.senpure.javafx.jfoenix.Jfoenix;
import com.senpure.javafx.jfoenix.JfoenixProperties;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * JfoenixAutoConfiguration
 *
 * @author senpure
 * @time 2020-07-09 18:33:37
 */
@ConditionalOnClass(JFXDecorator.class)
@AutoConfigureAfter(JavafxAutoConfiguration.class)
@EnableConfigurationProperties(JfoenixProperties.class)
public class JfoenixAutoConfiguration {

    private JfoenixProperties jfoenixProperties;

    private String shape = "M512 682c-128.024-99.309-255.626-199.040-384-298l384-298 384 298c-128.375 98.958-255.974 198.693-384 298zM512 792l314-246 70 54-384 298-384-298 70-54z";

    public JfoenixAutoConfiguration(JfoenixProperties jfoenixProperties) {
        this.jfoenixProperties = jfoenixProperties;
        Jfoenix.setJfoenixProperties(jfoenixProperties);
        JfoenixProperties.Decorator decorator = jfoenixProperties.getDecorator();
        if (decorator.isEnable()) {
            Javafx.setSceneFactory(new SceneFactory() {
                @Override
                public Scene get(Parent parent) {
                    JFXDecorator jfxDecorator = new JFXDecorator(Javafx.getPrimaryStage(), parent);
                    Jfoenix.setDecorator(jfxDecorator);
                    Scene scene = new Scene(jfxDecorator);
                    final ObservableList<String> stylesheets = scene.getStylesheets();
                    stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                            JFoenixResources.load("css/jfoenix-design.css").toExternalForm());
                    stylesheets.add(Jfoenix.class.getResource("css/jfoenix.css").toExternalForm());
                    stylesheets.addAll(parent.getStylesheets());
                    if (decorator.isStageIcon()) {
                        ObservableList<Image> images = Javafx.getPrimaryStage().getIcons();
                        if (!images.isEmpty()) {
                            ImageView imageView = new ImageView();
                            imageView.setFitHeight(16);
                            imageView.setFitWidth(16);
                            Image image = images.get(0);
                            if (images.size() > 1) {
                                double v = 999999999;
                                for (Image t : images) {
                                    double tw = Math.abs(t.getHeight() - 16);
                                    double th = Math.abs(t.getHeight() - 16);
                                    double tv = tw + th;
                                    if (tv < v) {
                                        image = t;
                                        v = tv;
                                    }
                                }
                            }
                            imageView.setImage(image);
                            jfxDecorator.setGraphic(imageView);

                        }

                    } else {
                        if (StringUtils.hasText(decorator.getSvgPathContent())) {
                            SVGGlyph svgGlyph;
                            if (decorator.getSvgPathContent().equals(shape)) {
                                svgGlyph = new SVGGlyph(decorator.getSvgPathContent());
                            } else {
                                svgGlyph = new SVGGlyph(decorator.getSvgPathContent(), Color.WHITE);


                                try {

                                    StringBuilder sb = new StringBuilder();
                                    sb.append(".jfx-decorator .jfx-decorator-title-container .jfx-svg-glyph {");
                                    sb.append("\n");

                                    sb.append("-fx-shape:").append("\"").append(decorator.getSvgPathContent()).append("\";");

                                    sb.append("\n");
                                    sb.append("}");
                                    File file = File.createTempFile("jfx-decorator-custom", ".css");
                                    file.deleteOnExit();

                                    FileOutputStream outputStream = new FileOutputStream(file);
                                    outputStream.write(sb.toString().getBytes());
                                    outputStream.flush();
                                    outputStream.close();

                                    jfxDecorator.getStylesheets().addAll(file.toURI().toURL().toExternalForm());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                            jfxDecorator.setGraphic(svgGlyph);

                        }

                    }

                    return scene;

                }
            });

        }
    }


    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(".jfx-decorator .jfx-decorator-title-container .jfx-svg-glyph {");

        sb.append("-fx-shape:").append("\"").append("8888").append("\";");

        sb.append("}");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
        InputStreamResource resource = new InputStreamResource(inputStream);

        try {
            System.out.println(resource.getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
