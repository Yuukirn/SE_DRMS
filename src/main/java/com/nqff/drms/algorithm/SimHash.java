<<<<<<< HEAD
//package com.nqff.drms.algorithm;
//
//import com.huaban.analysis.jieba.JiebaSegmenter;
//import com.huaban.analysis.jieba.SegToken;
//
//import java.math.BigInteger;
//import java.util.*;
//
//public class SimHash {
//    private JiebaSegmenter segmenter;
//    private List<Map<String, List<Long>>> storage;
//    private final int bitNum = 64;
//    private int fracCount = 4;
//    private int hammingThresh = 20;
//
//    public SimHash() {
//        this.Init();
//    }
//
//    public SimHash(int fracCount, int hammingThresh) {
//        this.fracCount = fracCount;
//        this.hammingThresh = hammingThresh;
//        this.Init();
//    }
//
//    private void Init() {
//        this.segmenter = new JiebaSegmenter();
//        this.storage = new ArrayList<>();
//        for (int i = 0; i < fracCount; i++) {
//            storage.add(new HashMap<>());
//        }
//    }
//
////    public boolean isSimilar(String content) {
////        Long simHash = calSimHash(content);
////        return isSimilar(simHash);
////    }
//
//    public boolean isSimilar(long sh1,long sh2) {
//        if (hamming(sh1, sh2) < hammingThresh) return true;
//        return false;
//    }
//
//    public boolean isSimilar(int dis) {
//        if (dis < hammingThresh) return true;
//        return false;
//    }
//
//
////    public boolean isSimilar(long simHash) {
////        List<String> lFrac = splitSimHash(simHash);
////        for (int i = 0; i < fracCount; i++) {
////            String frac = lFrac.get(i);
////            Map<String, List<Long>> fracMap = storage.get(i);
////            if (fracMap.containsKey(frac)) {
////                for (Long simHash2 : fracMap.get(frac)) {
////                    if (hamming(simHash, simHash2) < hammingThresh) {
////                        return true;
////                    }
////                }
////            }
////        }
////        return false;
////    }
//
//    public Long calSimHash(String content) {
//        String filterContent = content.trim().replaceAll("\\p{Punct}|\\p{Space}", "");
//        List<SegToken> lsegStr = segmenter.process(filterContent, JiebaSegmenter.SegMode.SEARCH);
//
//        Integer[] weight = new Integer[bitNum];
//        Arrays.fill(weight, 0);
//        for (SegToken st : lsegStr) {
//            long wordHash = Murmur3.hash64(st.word.getBytes());
//            for (int i = 0; i < bitNum; i++) {
//                if (((wordHash >> i) & 1) == 1) {
//                    weight[i] += 1;
//                } else {
//                    weight[i] -= 1;
//                }
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bitNum; i++) {
//            if (weight[i] > 0) {
//                sb.append(1);
//            } else {
//                sb.append(0);
//            }
//        }
//
//        return new BigInteger(sb.toString(), 2).longValue();
//    }
//
//    public int hamming(Long s1, Long s2) {
//        int dis = 0;
//        for (int i = 0; i < bitNum; i++) {
//            if ((s1 >> i & 1) != (s2 >> i & 1)) dis++;
//        }
//        return dis;
//    }
//
////    private int hamming(String s1, String s2) {
////        if (s1.length() != s2.length()) return 0;
////        int dis = 0;
////        for (int i = 0; i < s1.length(); i++) {
////            if (s1.charAt(i) != s2.charAt(i)) dis++;
////        }
////        return dis;
////    }
//
//    public void store(Long simhash, String content) {
//        List<String> lFrac = splitSimHash(simhash);
//        for (int i = 0; i < fracCount; i++) {
//            String frac = lFrac.get(i);
//            Map<String, List<Long>> fracMap = storage.get(i);
//            if (fracMap.containsKey(frac)) fracMap.get(frac).add(simhash);
//            else {
//                List<Long> ls = new ArrayList<>();
//                ls.add(simhash);
//                fracMap.put(frac, ls);
//            }
//        }
//    }
//
//    private List<String> splitSimHash(Long simHash) {
//        List<String> list = new ArrayList<>();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bitNum; i++) {
//            sb.append(simHash >> i & 1);
//            if ((i + 1) % fracCount == 0) {
//                list.add(sb.toString());
//                sb.setLength(0);
//            }
//        }
//        return list;
//    }
//}
=======
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
    private static JiebaSegmenter segmenter = new JiebaSegmenter();
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
     * @return
     */
    public static Long calSimHash(String content) {
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
>>>>>>> 93bec59fcb30e61920b3fe7eaa6be674f91104ca
