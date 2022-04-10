package com.senpure.io.generator.ui;

import com.senpure.javafx.Javafx;
import javafx.stage.Stage;

/**
 * UiContext
 *
 * @author senpure
 * @time 2019-07-09 21:58:06
 */
public class UiContext {
    public static Stage getPrimaryStage() {
        return Javafx.getPrimaryStage();
    }
}
