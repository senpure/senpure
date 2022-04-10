package com.senpure.cli;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * CommandProcess
 *
 * @author senpure
 * @time 2020-07-23 16:01:20
 */
public interface CommandProcess {

    /**
     * 反馈
     * @param feed
     */
    default void feed(String feed) {
        System.out.println(feed);
    }

    /**
     * 成功
     * @param feed
     */
    default void success(String feed) {

        feed(color(Color.BLUE, feed));
    }

    default void info(String feed) {
        feed(color(Color.CYAN, feed));
    }

    /**
     * 警告
     * @param feed
     */
    default void warn(String feed) {
        feed(color(Color.YELLOW, feed));
    }
    /**
     * 错误
     * @param feed
     */
    default void error(String feed) {
        feed(color(Color.RED, feed));
    }

    /**
     * 补全命令
     * @param options
     */
    default void completionOptions(@Nonnull List<Option> options) {

        for (Option option : options) {
            System.out.println(option.getValue());
        }
    }

    /**
     * 补全命令 这是一个完整的命令
     * @param command
     * @param fromMultiple 是否是多个选项  如（g |gold ）（-u | -user）
     */
    default void completion(String command, boolean fromMultiple) {
        System.out.println(command);
    }

    default String color(Color color, String element) {
        return element;
    }

    default String color(Color color, int element) {
        return color(color, String.valueOf(element));
    }

    default String color(Color color, long element) {
        return color(color, String.valueOf(element));
    }

    default String color(Color color, double element) {
        return color(color, String.valueOf(element));
    }

    default String color(Color color, float element) {
        return color(color, String.valueOf(element));
    }
}
