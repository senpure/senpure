package org.jline.reader.impl;

import org.jline.reader.Candidate;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class LineReaderImplV2 extends  LineReaderImpl{
    public LineReaderImplV2(Terminal terminal) throws IOException {
        super(terminal);
    }

    public LineReaderImplV2(Terminal terminal, String appName) throws IOException {
        super(terminal, appName);
    }

    public LineReaderImplV2(Terminal terminal, String appName, Map<String, Object> variables) {
        super(terminal, appName, variables);
    }

    @Override
    protected PostResult computePost(List<Candidate> possible, Candidate selection, List<Candidate> ordered, String completed, Function<String, Integer> wcwidth, int width, boolean autoGroup, boolean groupName, boolean rowsFirst) {
        List<Object> strings = new ArrayList<>();
        if (groupName) {
            Comparator<String> groupComparator = getGroupComparator();
            Map<String, Map<String, Candidate>> sorted;
            sorted = groupComparator != null
                    ? new TreeMap<>(groupComparator)
                    : new LinkedHashMap<>();
            for (Candidate cand : possible) {
                String group = cand.group();
                sorted.computeIfAbsent(group != null ? group : "", s -> new LinkedHashMap<>())
                        .put(cand.value(), cand);
            }
            for (Map.Entry<String, Map<String, Candidate>> entry : sorted.entrySet()) {
                String group = entry.getKey();
                if (group.isEmpty() && sorted.size() > 1) {
                    group = getOthersGroupName();
                }
                if (!group.isEmpty() && autoGroup) {
                    strings.add(group);
                }
                strings.add(new ArrayList<>(entry.getValue().values()));
                if (ordered != null) {
                    ordered.addAll(entry.getValue().values());
                }
            }
        } else {
            Set<String> groups = new LinkedHashSet<>();
           // TreeMap<String, Candidate> sorted = new TreeMap<>();
            for (Candidate cand : possible) {
                String group = cand.group();
                if (group != null) {
                    groups.add(group);
                }
              //  sorted.put(cand.value(), cand);

            }
            if (autoGroup) {
                strings.addAll(groups);
            }
            //strings.add(new ArrayList<>(sorted.values()));
            strings.add(new ArrayList<>(possible));
            if (ordered != null) {
                //ordered.addAll(sorted.values());
                ordered.addAll(possible);
            }
        }
        return toColumns(strings, selection, completed, wcwidth, width, rowsFirst);
    }
}
