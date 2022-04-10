package com.senpure.io.generator.ui;

import com.senpure.base.AppEvn;
import com.senpure.io.generator.SilenceBoot;

import java.util.Objects;

/**
 * Boot
 *
 * @author senpure
 * @time 2020-04-20 16:28:40
 */
public class Boot {

    public static void main(String[] args) {
        AppEvn.markClassRootPath();
        AppEvn.installAnsiConsole();
        if (Objects.equals(System.getProperty("silence"), "true")) {
            SilenceBoot.main(args);
        } else {
            UiBoot.main(args);
        }
    }


}
