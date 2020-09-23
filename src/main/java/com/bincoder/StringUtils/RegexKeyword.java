package com.bincoder.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexKeyword {

    public static void main(String[] args) {
        String input = "我在[[中]][[国]]";
        // ?匹配一个字符，+匹配一个或多个
        Matcher matcher = Pattern.compile("(?<=\\[\\[)(\\S?)(?=\\]\\])").matcher(input);
        while (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
