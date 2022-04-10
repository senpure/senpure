package com.senpure.javafx;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * SceneFactory
 *
 * @author senpure
 * @time 2020-07-09 18:47:30
 */
public interface SceneFactory {

    default Scene get(Parent parent) {
        return new Scene(parent);
    }

}
