package com.senpure.io.generator.ui.view;


import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * AnimationController
 *
 * @author senpure
 * @time 2019-07-11 17:30:11
 */

public class AnimationController implements Initializable {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private Button btnAnimation;
    @FXML
    private Separator separatorA;
    @FXML
    private Separator separatorB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void animation() {
        Path path = new Path();//创建一个路径对象



        Node node = btnAnimation;
        double x = node.getLayoutX();
        double y = node.getLayoutY();



        x=20;
        y=5;
        logger.debug("x = {} y ={}", x, y);

        double value = 20;
        path.getElements().add(new MoveTo(x, y));//从哪个位置开始动画，一般来说给组件的默认位置就行
        path.getElements().add(new LineTo(100 , 0 ));//添加一个向左移动的路径
       // path.getElements().add(new LineTo(0 ,100 ));//添加一个向右移动的路径  这样就完成第一遍摇头
        path.getElements().add(new LineTo(x, y));
       // path.getElements().add(new LineTo(x, y + value));//最后移动到原来的位置
        PathTransition pathTransition = new PathTransition();//创建一个动画对象
        pathTransition.setDuration(Duration.seconds(3));//动画持续时间 0.5秒
        pathTransition.setPath(path);//把我们设置好的动画路径放入里面
        pathTransition.setNode(node);//给动画添加组件，让某个组件来完成这个动画
        pathTransition.setCycleCount(1);//执行1遍
        pathTransition.setAutoReverse(true);
        pathTransition.play();//执行动画

        //node.setLayoutX(x);
        //node.setLayoutY(y);
    }
}
