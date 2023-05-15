package com.nqff.drms.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SimHash {
    private static final int bitNum = 64;
    static private HashSet<String> stopWordsSet;
    private static int fracCount = 4;
    private static int hammingThresh = 20;

    public SimHash(HashSet<String> set) {
        if (stopWordsSet == null) stopWordsSet = set;
    }

    public static boolean isSimilar(long sh1, long sh2) {
        if (hamming(sh1, sh2) < hammingThresh) return true;
        return false;
    }

    public static boolean isSimilar(int dis) {
        if (dis < hammingThresh) return true;
        return false;
    }

    /**
     * 计算文本的simHash值
     *
     * @param content
     * @return 文本simHash值
     */
    public static Long calSimHash(String content) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String filterContent = content.trim().replaceAll("\\p{Punct}|\\p{Space}", "");
        List<String> segments = segmenter.sentenceProcess(filterContent);
        Integer[] weight = new Integer[bitNum];
        Arrays.fill(weight, 0);
        for (String segment : segments) {
            if (!stopWordsSet.contains(segment) && segment.length() > 1) {
                long wordHash = Murmur3.hash64(segment.getBytes());
                for (int i = 0; i < bitNum; i++) {
                    if (((wordHash >> i) & 1) == 1) {
                        weight[i] += 1;
                    } else {
                        weight[i] -= 1;
                    }
                }
            } else {
                segments.remove(segment);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitNum; i++) {
            if (weight[i] > 0) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }

        return new BigInteger(sb.toString(), 2).longValue();
    }

    public static int hamming(Long s1, Long s2) {
        int dis = 0;
        for (int i = 0; i < bitNum; i++) {
            if ((s1 >> i & 1) != (s2 >> i & 1)) dis++;
        }
        return dis;
    }

    private static int hamming(String s1, String s2) {
        if (s1.length() != s2.length()) return 0;
        int dis = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) dis++;
        }
        return dis;
    }

    private static List<String> splitSimHash(Long simHash) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitNum; i++) {
            sb.append(simHash >> i & 1);
            if ((i + 1) % fracCount == 0) {
                list.add(sb.toString());
                sb.setLength(0);
            }
        }
        return list;
    }
}