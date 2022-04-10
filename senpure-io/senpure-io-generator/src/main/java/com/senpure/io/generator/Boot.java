package com.senpure.io.generator;

import com.senpure.base.AppEvn;

/**
 * Boot
 *
 * @author senpure
 * @time 2019-05-15 14:26:10
 */

public class Boot {



    public static void main(String[] args) {
        AppEvn.markClassRootPath();
        AppEvn.installAnsiConsole();
        SilenceBoot.main(args);
//        if (Objects.equals(System.getProperty("silence"), "true")) {
//            SilenceBoot.main(args);
//        } else {
//            UiBoot.main(args);
//        }
        // SpringApplication.run(Boot.class, args);
    }



}
