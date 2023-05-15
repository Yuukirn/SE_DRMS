package com.nqff.drms.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.nqff.drms.pojo.Keyword;

import java.util.*;

public class TFIDFAnalyzer {
    static private HashSet<String> stopWordsSet;
    static private HashMap<String, Double> idfMap;
    static private double idfMedian;

    public TFIDFAnalyzer(HashSet<String> set, HashMap<String, Double> map, double idfMedian) {
        if (stopWordsSet == null) stopWordsSet = set;
        if (idfMap == null) idfMap = map;
        this.idfMedian = idfMedian;
    }

    /**
     * 获取文本关键词
     *
     * @param content
     * @param N       需要返回的关键词数
     * @return
     */
    public static List<Keyword> getKeyWord(String content, int N) {
        Map<String, Double> tfMap = getTFMap(content);
        List<Keyword> keywordList = new ArrayList<>();

        for (String word : tfMap.keySet()) {
            double tf = tfMap.get(word);
            double idf;
            if (idfMap.containsKey(word)) {
                idf = idfMap.get(word);
            } else {
                idf = idfMedian;
            }
            double tfidf = tf * idf;
            Keyword kw = new Keyword();
            kw.setName(word);
            kw.setWeight(tfidf);
            keywordList.add(kw);
        }

        Collections.sort(keywordList);

        if (keywordList.size() > N) {
            int num = keywordList.size() - N;
            for (int i = 0; i < num; i++) {
                keywordList.remove(N);
            }
        }
//        for (Keyword kw : keywordList) System.out.println(kw);
        return keywordList;
    }

    private static Map<String, Double> getTFMap(String content) {
        Map<String, Double> tfMap = new HashMap<>();
        if (content == null || content == "") {
            return tfMap;
        }

        JiebaSegmenter segmenter = new JiebaSegmenter();
        String filterContent = content.trim().replaceAll("\\p{Punct}|\\p{Space}", "");
        List<String> segments = segmenter.sentenceProcess(filterContent);

        int segNum = 0;
        for (String segment : segments) {
            if (!stopWordsSet.contains(segment) && segment.length() > 1) {
                segNum++;
                tfMap.put(segment, tfMap.getOrDefault(segment, 0d) + 1);
            }
        }

        for (String seg : tfMap.keySet()) {
            tfMap.put(seg, tfMap.get(seg) * 0.1 / segNum);
        }

        return tfMap;
    }
}
