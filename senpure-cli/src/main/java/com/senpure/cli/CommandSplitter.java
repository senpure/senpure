package com.senpure.cli;

import java.util.StringTokenizer;

/**
 * CommandSpliter
 *
 * @author senpure
 * @time 2020-08-14 12:00:15
 */
public interface CommandSplitter {


    default String[] split(String command) {
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdArray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdArray[i] = st.nextToken();
        }
        return cmdArray;
    }

}
