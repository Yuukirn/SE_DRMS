package com.nqff.drms.algorithm;

import com.nqff.drms.pojo.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TFIDFAnalyzer {
    static private HashSet<String> stopWordsSet;
    static private HashMap<String, Double> idfMap;

    public TFIDFAnalyzer(HashSet<String> set, HashMap<String, Double> map) {
        if (stopWordsSet == null) stopWordsSet = set;
        if (idfMap == null) idfMap = map;
    }

    /**
     * 获取文本关键词
     *
     * @param content
     * @param N       需要返回的关键词数
     * @return
     */
    public List<Keyword> getKeyWord(String content, int N) {
        List<Keyword> keywords = new ArrayList<>();

        return keywords;
    }
}
