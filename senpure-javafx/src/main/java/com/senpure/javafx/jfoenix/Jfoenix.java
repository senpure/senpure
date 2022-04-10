package com.senpure.javafx.jfoenix;

import com.jfoenix.controls.JFXDecorator;

/**
 * Jfoenix
 *
 * @author senpure
 * @time 2020-07-09 18:38:47
 */
public class Jfoenix {

    private static JfoenixProperties jfoenixProperties;
    private static  JFXDecorator decorator;

    public static JFXDecorator getDecorator() {
        return decorator;
    }

    public static void setDecorator(JFXDecorator decorator) {
        Jfoenix.decorator = decorator;
    }

    public static JfoenixProperties getJfoenixProperties() {
        return jfoenixProperties;
    }

    public static void setJfoenixProperties(JfoenixProperties jfoenixProperties) {
        Jfoenix.jfoenixProperties = jfoenixProperties;
    }
}
