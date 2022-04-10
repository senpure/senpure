package com.senpure.javafx.jfoenix;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JfoenixProperties
 *
 * @author senpure
 * @time 2020-07-09 18:23:24
 */
@ConfigurationProperties(prefix = "javafx.jfoenix")
public class JfoenixProperties {


    private Decorator decorator = new Decorator();

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    public static class Decorator {

        /**
         * 启用JFXDecorator 装饰
         */
        private boolean enable = true;
        /**
         * 使用stage的图标
         */
        private boolean stageIcon = true;

        private String svgPathContent;


        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public boolean isStageIcon() {
            return stageIcon;
        }

        public void setStageIcon(boolean stageIcon) {
            this.stageIcon = stageIcon;
        }

        public String getSvgPathContent() {
            return svgPathContent;
        }

        public void setSvgPathContent(String svgPathContent) {
            this.svgPathContent = svgPathContent;
        }

    }
}
