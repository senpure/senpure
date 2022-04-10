提供在javafx中使用springboot 的功能

#### 快速开始

```
 @JavafxBoot
 public class ExampleBoot extends JavafxSpringBootApplication {
     
     public static void main(String[] args) {
         launch(ExampleBoot.class, MainView.class, args);
     }
     
 }
```

#### 启动过渡动画
springboot启动会消耗时间,默认使用一个过渡画面，在资源文件加入splash(.png,.jpg)改变图片样式

#### 约定
如果你定义了一个MainView 那么默认fxml定制为相同目录的main.fxml css为相同目录的main.css除非你手动指定



### 注解
1. `@JavafxBoot` 同 @SpringBootApplication
2. `@View` 定义一个视图bean,默认懒加载(fxml,css,国家化属性等)遵循约定大于配置，可以省略该注解
3. `@Controller` 定义一个控制bean,默认赖加载,可以省略该注解


