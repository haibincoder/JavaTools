package com.bincoder.StringUtils;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

import java.util.*;

public class KeywordMatch {

    /**
     * 构建ac自动机
     */
    public static AhoCorasickDoubleArrayTrie<String> buildAcdt(List<String> keywords){
        AhoCorasickDoubleArrayTrie<String> acdt = new AhoCorasickDoubleArrayTrie<>();
        TreeMap<String, String> map = new TreeMap<>();
        for(String keyword : keywords){
            map.put(keyword, keyword);
        }
        acdt.build(map);
        return acdt;
    }

    public static Set<String> highLight(String originText, AhoCorasickDoubleArrayTrie<String> acdt){
        Set<String> keywords = new HashSet<>();
        // ac算法匹配关键词
        acdt.parseText(originText, (begin, end, value)->{
           keywords.add(value);
        });

        return keywords;
    }

    public static void main(String[] args) {
        String text = "我想买苹果手机，请问哪里可以买苹果手机";
        List<String> keywords = Arrays.asList("苹果", "苹果手机", "哪里", "百度", "腾讯");

        AhoCorasickDoubleArrayTrie<String> acdt = KeywordMatch.buildAcdt(keywords);
        Set<String> result = KeywordMatch.highLight(text, acdt);
        System.out.println("result:" + result.toString());
    }

}
