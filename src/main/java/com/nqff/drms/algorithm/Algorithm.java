package com.nqff.drms.algorithm;

import com.nqff.drms.pojo.Keyword;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


@RestController
@RequestMapping(path = "/algorithm")
@Tag(name = "算法")
public class Algorithm {
    static protected double idfMedian;
    static private HashMap<String, Double> idfMap;
    static private HashSet<String> stopWordsSet;
    static private TFIDFAnalyzer tfidfAnalyzer;
    static private SimHash simHash;

    private int KeyWordNum = 5;

    public Algorithm() {
        if (stopWordsSet == null) {
            stopWordsSet = new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/stop_words.txt"));
//            System.out.println(stopWordsSet);
        }
        if (idfMap == null) {
            idfMap = new HashMap<>();
            loadIDFMap(idfMap, this.getClass().getResourceAsStream("/idf_dict.txt"));
//            System.out.println(idfMap.entrySet());
        }
        if (tfidfAnalyzer == null) {
            tfidfAnalyzer = new TFIDFAnalyzer(stopWordsSet, idfMap, idfMedian);
        }
        if (simHash == null) {
            simHash = new SimHash(stopWordsSet);
        }
    }

    public long getSimHash(String content) {
        return SimHash.calSimHash(content);
    }

    /**
     * 获取文本关键字
     *
     * @param content
     * @return 默认返回权重最大的5个关键字，若不足5个则返回全部关键字
     */
    public List<Keyword> getKeyWord(String content) {
        return tfidfAnalyzer.getKeyWord(content, KeyWordNum);
    }

    /**
     * 获取文本关键字
     *
     * @param content
     * @param n       * @return  返回权重最大的n个关键字，若不足n个则返回全部关键字
     */
    public List<Keyword> getKeyWord(String content, int n) {
        return tfidfAnalyzer.getKeyWord(content, n);
    }

    /**
     * 加载停用词表
     *
     * @param set
     * @param in
     */
    private void loadStopWords(Set<String> set, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                set.add(line.trim());
            }
            try {
                bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载idf字典
     *
     * @param map
     * @param in
     */
    private void loadIDFMap(Map<String, Double> map, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            while ((line = bufr.readLine()) != null) {
                String[] kv = line.trim().split(" ");
                map.put(kv[0], Double.parseDouble(kv[1]));
            }
            try {
                bufr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 计算idf值的中位数
            List<Double> idfList = new ArrayList<>(map.values());
            Collections.sort(idfList);
            idfMedian = idfList.get(idfList.size() / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}