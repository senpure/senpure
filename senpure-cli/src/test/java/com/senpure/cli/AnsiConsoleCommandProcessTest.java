package com.senpure.cli;

import org.junit.jupiter.api.Test;



class AnsiConsoleCommandProcessTest {

    @Test
    void color() {

        AnsiConsoleCommandProcess  commandProcess = new AnsiConsoleCommandProcess(true);


        for (Color value : Color.values()) {
            System.out.println(value.toString()+":"+commandProcess.color(value,"888")+"]");
        }
    }
}