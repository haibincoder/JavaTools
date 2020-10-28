package com.bincoder.StringUtils;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class HighLight {
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

    public static String highLight(String originText, AhoCorasickDoubleArrayTrie<String> acdt){
        List<int[]> hitLocationList = new ArrayList<>();
        // ac算法匹配关键词
        acdt.parseText(originText, (begin, end, value)->{
            int[] indexPair = new int[2];
            indexPair[0] = begin;
            indexPair[1] = end-1;
            hitLocationList.add(indexPair);
        });
        // 构建bitmap
        byte[] posStatus = new byte[originText.length()];
        for(int[] item : hitLocationList){
            for(int i=item[0]; i<item[1]; i++){
                posStatus[i] = 1;
            }
        }
        // 字符串拼接
        int lastStatus = 0;
        char[] charArray = originText.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<posStatus.length; i++){
            if(posStatus[i] == lastStatus){
                stringBuilder.append(charArray[i]);
            }else if(0 == lastStatus){
                stringBuilder.append("[[").append(charArray[i]);
                lastStatus = 1;
            }else if(1 == lastStatus){
                stringBuilder.append("]]").append(charArray[i]);
                lastStatus = 0;
            }
            if(i == posStatus.length-1 && 1 == lastStatus){
                stringBuilder.append("]]");
            }
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String text = "我想买苹果手机，请问哪里可以买苹果手机";
        List<String> keywords = Arrays.asList("苹果", "苹果手机", "哪里");

        AhoCorasickDoubleArrayTrie<String> acdt = KeywordMatch.buildAcdt(keywords);
        String result = HighLight.highLight(text, acdt);
        System.out.println("inpu:" + text);
        System.out.println("result:" + result);

        long start = System.currentTimeMillis();
        for(int i=0; i<100000; i++){
            result = HighLight.highLight(text, acdt);
        }
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("耗时:" + total + "ms");

    }
}
