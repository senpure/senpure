package com.senpure.io.generator.habit;

import com.senpure.base.AppEvn;
import com.senpure.base.util.JSON;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * HabitUtil
 *
 * @author senpure
 * @time 2019-07-08 14:44:10
 */
public class HabitUtil {

    private static Logger logger = LoggerFactory.getLogger(HabitUtil.class);


    private static Habit habit;

    private static Habit loadHabit() {
        File save = new File(AppEvn.getClassRootPath(), "config/habit.json");
        logger.debug("行为配置文件路径{}", save.getAbsolutePath());
        if (!save.exists()) {
            return newHabit();
        }
        try {
            String config = FileUtils.readFileToString(save, StandardCharsets.UTF_8);
            Habit habit = JSON.parseObject(config, Habit.class);
            if (habit == null) {
                return newHabit();
            }
            HabitUtil.habit = habit;
            checkConfig(getUseConfig());
            return habit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Habit newHabit() {
        Habit habit = new Habit();
        ProjectConfig config = new ProjectConfig();

        configInitValue(config);
        habit.setUserProject(config.getProjectName());
        habit.getConfigs().add(config);
        HabitUtil.habit = habit;
        return habit;
    }

    public static Habit getHabit() {
        if (habit != null) {
            return habit;
        }
        loadHabit();
        return habit;
    }

    public static ProjectConfig getUseConfig() {
        if (habit == null) {
            loadHabit();
        }
        for (ProjectConfig config : habit.getConfigs()) {
            if (Objects.equals(config.getProjectName(), habit.getUserProject())) {
                return config;
            }
        }
        return habit.getConfigs().get(0);
    }

    public static void configInitValue(ProjectConfig projectConfig) {

        projectConfig.setProtocolFileChooserPath(AppEvn.getClassRootPath());
        projectConfig.setProtocolDirectoryChooserPath(AppEvn.getClassRootPath());
        projectConfig.setProjectName("myProject");

        projectConfig.getJavaConfig().initValue();

        projectConfig.getLuaConfig().initValue();

        projectConfig.getJsConfig().initValue();

    }

    public static void checkConfig(ProjectConfig projectConfig) {
        if (!new File(projectConfig.getProtocolFileChooserPath()).exists()) {
            projectConfig.setProtocolFileChooserPath(AppEvn.getClassRootPath());
        }
        if (!new File(projectConfig.getProtocolDirectoryChooserPath()).exists()) {
            projectConfig.setProtocolDirectoryChooserPath(AppEvn.getClassRootPath());
        }

        projectConfig.getJavaConfig().checkSelf();

        projectConfig.getLuaConfig().checkSelf();
        projectConfig.getJsConfig().checkSelf();
    }

    public static void saveHabit(Habit habit) {
        logger.debug("save habit");
        String json = JSON.toJSONString(habit, true);
        File save = new File(AppEvn.getClassRootPath(), "config/habit.json");
        File parent = save.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                return;
            }
        }
        try {
            FileUtils.writeStringToFile(save, json);
        } catch (IOException e) {
            e.printStackTrace();
            //logger.error("保存习惯配置出错", e);
        }
        logger.debug("save habit success");
    }
}
