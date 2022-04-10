package com.senpure.javafx;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * JavafxProperties
 *
 * @author senpure
 * @time 2020-06-19 14:46:45
 */
public class JavafxProperties {
    public static final String DEFAULT_BASENAME = "i18n/app";
    public static final String DEFAULT_CSS = "app.css";
    /**
     * 应用标题支持%国际化,优先级低于view中的配置
     */
    private String title = "javafx";
    /**
     * 国际化locale
     */
    private Locale locale = Locale.getDefault();
    /**
     * 应用图标
     */
    private List<String> icons;

    /**
     * 使用编码
     */
    private Charset encoding = StandardCharsets.UTF_8;
    /**
     * 全局css
     */
    private String css;
    /**
     * 全局basename
     */
    private List<String> basenames;

    /**
     * 过渡场景关闭动画
     */
    private boolean splashCloseAnimationEnable = true;
    /**
     * 主舞台启动动画
     */
    private boolean primaryStageShowAnimationEnable = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public List<String> getBasenames() {
        return basenames;
    }

    public void setBasenames(List<String> basenames) {
        this.basenames = basenames;
    }

    public boolean isSplashCloseAnimationEnable() {
        return splashCloseAnimationEnable;
    }

    public void setSplashCloseAnimationEnable(boolean splashCloseAnimationEnable) {
        this.splashCloseAnimationEnable = splashCloseAnimationEnable;
    }

    public boolean isPrimaryStageShowAnimationEnable() {
        return primaryStageShowAnimationEnable;
    }

    public void setPrimaryStageShowAnimationEnable(boolean primaryStageShowAnimationEnable) {
        this.primaryStageShowAnimationEnable = primaryStageShowAnimationEnable;
    }
}
