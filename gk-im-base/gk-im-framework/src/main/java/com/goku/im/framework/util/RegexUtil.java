package com.goku.im.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author moueimei
 */
public class RegexUtil {
    public static Boolean isMatch(String input, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }

    public static String match(String input, String regEx) {
        return match(input, regEx, 1);
    }

    public static String match(String input, String regEx, int index) {
        return match(input, regEx, index, 0);
    }

    public static String match(String input, String regEx, int index, int flag) {
        Pattern p = Pattern.compile(regEx, flag);
        Matcher m = p.matcher(input);
        boolean rs = m.find();
        if (rs)
            if (m.groupCount() >= 1)
                return m.group(index);

        return null;
    }

    public static List<String> matchItems(String input, String regEx) {
        return matchItems(input, regEx, 0);
    }

    public static List<String> matchItems(String input, String regEx, int flag) {
        List<String> matches = new ArrayList<String>();
        Pattern p = Pattern.compile(regEx, flag);
        Matcher m = p.matcher(input);
        while (m.find())
            matches.add(m.group(1));
        return matches;
    }

    public static List<String> multiMatch(String input, String regEx) {
        List<String> matches = null;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        if (m.find()) {
            matches = new ArrayList<String>();
            int count = m.groupCount();
            for (int i = 0; i <= count; i++)
                matches.add(m.group(i));
        }

        return matches;
    }
}