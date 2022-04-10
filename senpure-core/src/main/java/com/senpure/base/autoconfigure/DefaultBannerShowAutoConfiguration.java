package com.senpure.base.autoconfigure;

import com.senpure.base.AppEvn;
import com.senpure.base.util.BannerShow;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@EnableConfigurationProperties(DefaultBannerShowAutoConfiguration.BannerProperties.class)
public class DefaultBannerShowAutoConfiguration {

    //是否主动设置了减半显示
    private static boolean activeHalf;

    @Bean
    @Order
    public DefaultBannerShow defaultBannerShow(BannerProperties bannerProperties) {
        return new DefaultBannerShow(bannerProperties);
    }

    static class DefaultBannerShow implements ApplicationRunner {
        private final BannerProperties bannerProperties;

        public DefaultBannerShow(BannerProperties bannerProperties) {
            this.bannerProperties = bannerProperties;
        }

        @Override
        public void run(ApplicationArguments applicationArguments) throws Exception {
            if (bannerProperties.isEnable()) {
                if (bannerProperties.isTableStyle()) {
                    BannerShow.setDownLeft("╚");
                    BannerShow.setDownRight("╝");
                    BannerShow.setTopLeft("╔");
                    BannerShow.setTopRight("╗");
                    BannerShow.setTopDown("═");
                    BannerShow.setLeftRight("║");
                    if (activeHalf) {
                        BannerShow.setTopDownHalf(bannerProperties.isTopDownHalf());
                    } else {
                        if (AppEvn.isWindowsOS()) {
                            BannerShow.setTopDownHalf(true);
                        }
                    }
                } else {
                    BannerShow.setDownLeft(bannerProperties.getDownLeft());
                    BannerShow.setDownRight(bannerProperties.getDownRight());
                    BannerShow.setTopLeft(bannerProperties.getTopLeft());
                    BannerShow.setTopDown(bannerProperties.getTopDown());
                    BannerShow.setTopDown(bannerProperties.getTopDown());
                    BannerShow.setLeftRight(bannerProperties.getLeftRight());
                    BannerShow.setTopDownHalf(bannerProperties.isTopDownHalf());
                }

                BannerShow.show();
                Thread.sleep(1500);
            }

        }
    }

    @ConfigurationProperties(prefix = "senpure.default.banner")
    static
    class BannerProperties {
        /**
         * spring启动完成之后是否打印banner
         */
        private boolean enable = true;
        /**
         * 使用制表符风格，其他的字符配置将会失效
         */
        private boolean tableStyle;
        private String topLeft = "*";
        private String topRight = "*";
        private String downLeft = "*";
        private String downRight = "*";
        private String topDown = "*";
        /**
         * 宽字符不同的控制台有不同的表现形式，是否减半显示
         */
        private boolean topDownHalf;
        private String leftRight = "*";


        public BannerProperties() {
        }


        public void setTopDownHalf(boolean topDownHalf) {
            this.topDownHalf = topDownHalf;
            activeHalf = true;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getTopLeft() {
            return topLeft;
        }

        public void setTopLeft(String topLeft) {
            this.topLeft = topLeft;
        }

        public String getTopRight() {
            return topRight;
        }

        public void setTopRight(String topRight) {
            this.topRight = topRight;
        }

        public String getDownLeft() {
            return downLeft;
        }

        public void setDownLeft(String downLeft) {
            this.downLeft = downLeft;
        }

        public String getDownRight() {
            return downRight;
        }

        public void setDownRight(String downRight) {
            this.downRight = downRight;
        }

        public String getTopDown() {
            return topDown;
        }

        public void setTopDown(String topDown) {
            this.topDown = topDown;
        }

        public String getLeftRight() {
            return leftRight;
        }

        public void setLeftRight(String leftRight) {
            this.leftRight = leftRight;
        }

        public boolean isTopDownHalf() {
            return topDownHalf;
        }

        public boolean isTableStyle() {
            return tableStyle;
        }

        public void setTableStyle(boolean tableStyle) {
            this.tableStyle = tableStyle;
        }

    }
}
