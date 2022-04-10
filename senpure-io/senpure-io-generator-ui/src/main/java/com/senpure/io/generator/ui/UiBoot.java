package com.senpure.io.generator.ui;

import com.senpure.base.AppEvn;
import com.senpure.io.generator.habit.HabitUtil;
import com.senpure.io.generator.ui.view.MainController;
import com.senpure.io.generator.util.NoteUtil;
import com.senpure.javafx.JavafxSpringBootApplication;
import com.senpure.javafx.SpringBefore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;


/**
 * UiBoot
 *
 * @author senpure
 * @time 2019-07-05 16:01:04
 */
@SpringBootApplication
public class UiBoot extends JavafxSpringBootApplication {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MainController mainController;


    public static void main(String[] args) {

        launch(UiBoot.class, MainController.class,new SpringBefore(){

        }, args);
    }


    @Override
    protected Thread.UncaughtExceptionHandler uncaughtExceptionHandler() {
        return (t, e) -> {
            logger.error("出现错误", e);
            if (AppEvn.isWindowsOS()) {
                File logFile = new File(AppEvn.getClassRootPath(), "logs/generator.log");
                if (!AppEvn.classInJar(getClass())) {
                    logFile = new File(System.getProperty("user.dir"), "logs/generator.log");
                }
                NoteUtil.openNote(logFile, (int) (e.getStackTrace().length * 1.6));
            }
        };
    }

    @PreDestroy
    public void destroy() {
        try {
            mainController.updateConfig();
            HabitUtil.saveHabit(HabitUtil.getHabit());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
