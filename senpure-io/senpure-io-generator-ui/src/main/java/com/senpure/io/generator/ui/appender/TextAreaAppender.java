package com.senpure.io.generator.ui.appender;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * TextAreaAppender
 *
 * @author senpure
 * @time 2019-07-10 09:38:46
 */
@Plugin(name = "TextArea", category = "Core", elementType = "appender", printObject = true)
public class TextAreaAppender extends AbstractAppender {

    private static TextArea textArea;

    private static int lineLen = 88;
    private static String newLine = "\n";
    private static String space = "    ";
    private static BlockingQueue<String> queue = new LinkedBlockingDeque();

    static {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (textArea != null) {

                        try {
                            String str = queue.take();

                            Platform.runLater(() -> textArea.insertText(0, str));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.setName("textArea-thread");
        thread.setDaemon(true);
        thread.start();
    }

    private static String getStr(String str) {
        StringBuilder out = new StringBuilder();
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isChinese(c)) {
                len += 2;
            } else {
                len++;
            }
            if (len == lineLen) {
                out.append(c);
                if (i < out.length() - 1) {
                    out.append(newLine);
                    out.append(space);

                    len = 0;
                }
            } else if (len > lineLen) {
                out.append(newLine).append(space);
                out.append(c);
                len = 2;
            } else {
                out.append(c);
                if (c == '\n') {
//                                        if (i < out.length() - 1) {
//                                            out.append(space);
//                                        }
                    len = 0;
                }
            }
        }
        return out.toString();
    }

    private static boolean isChinese(char c) {
        if (c >= 0x4E00 && c <= 0x9FA5) {
            return true;
        }
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }


        return false;
    }

    public static void setTextArea(TextArea textArea) {
        // lineLen=textArea.getPrefColumnCount();
        TextAreaAppender.textArea = textArea;
    }

    protected TextAreaAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @Override
    public void append(LogEvent event) {
        queue.offer(new String(getLayout().toByteArray(event)));

    }


    @PluginFactory
    public static TextAreaAppender createAppender(@PluginAttribute("name") String name,
                                                  @PluginElement("Filter") final Filter filter,
                                                  @PluginElement("Layout") Layout<? extends Serializable> layout

    ) {

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new TextAreaAppender(name, filter, layout, true, null);
    }


    public static void main(String[] args) {

        String str = "1345\naaa";
        System.out.println(str);

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '\n') {

                System.out.println("======");
            }
        }
    }
}
