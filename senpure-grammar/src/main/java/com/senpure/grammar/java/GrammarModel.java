package com.senpure.grammar.java;

import java.util.ArrayList;
import java.util.List;

/**
 * GrammarModel
 *
 * @author senpure
 * @time 2020-05-16 14:38:38
 */
public class GrammarModel {

    private static final String STATIC_FILED = "STATIC_FILED_VALUE";
    private final static String STATIC_FILED2 = "STATIC_FILED2_VALUE";
    final static private String STATIC_FILED3 = "STATIC_FILED3_VALUE";
    // agsex
    private int[] ags;
    /**
     * 年龄
     */
    private int age;//age_end
    //姓名
    private String name;
    //书本
    //书本==
    public String book;
    //authors
    protected List<String> authors = new ArrayList<>();




    class InnerClassFirst {
        class InnerClassFirstInnerClass {
        }
    }

    class InnerClassSecond {
        class InnerClassSecondInnerClass {
        }
    }
}

class OutClass {

}
