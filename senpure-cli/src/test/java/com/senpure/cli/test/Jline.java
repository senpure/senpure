package com.senpure.cli.test;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.List;

public class Jline {

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .build();

        Completer commandCompleter=new Completer() {
            @Override
            public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {

            }
        };

    }
}
