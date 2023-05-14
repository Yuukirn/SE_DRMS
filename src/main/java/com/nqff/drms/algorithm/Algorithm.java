<<<<<<< HEAD
//package com.nqff.drms.algorithm;
//
//
//import com.nqff.drms.utils.Result;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping(path = "/algorithm")
//@Tag(name = "算法")
//public class Algorithm {
//    private final SimHash simHash = new SimHash(4, 20);
//    private int similarExampleNumber = 3;
//    @Autowired
//    private ExampleDao exampleDao;
////    private Murmur3 murmur3;
//
//    @Operation(summary = "查询相似案例", security = {@SecurityRequirement(name = "Authorization")})
//    @GetMapping("/{description}")
//    public Result getSimilarExample(@PathVariable String description) {
//        List<Example> examples = selectSimilarExampleByPlanDescription(description);
//        if (examples == null) {
//            return Result.FAIL("No similar examples found", null);
//        }
//        return Result.SUCCESS(examples);
//    }
//
//
//    public long getSimHash(String content) {
//        return simHash.calSimHash(content);
//    }
//
//
//    private List<Example> selectSimilarExampleByPlanDescription(String description) {
//        class Tuple<Example, Integer> {
//            Example e;
//            Integer dis;
//
//            public Tuple(Example e, Integer dis) {
//                this.e = e;
//                this.dis = dis;
//            }
//        }
//
//        long sh1 = simHash.calSimHash(description);
////        System.out.println(sh1);
//        List<Example> le = exampleDao.selectList(null);
//        List<Example> lsimilarE = new ArrayList<>();
//        Map<Integer, Tuple<Example, Integer>> mSimilarExamples = new HashMap<>();
//
//        for (int i = 0; i < similarExampleNumber; i++) {
//            mSimilarExamples.put(i, null);
//        }
//
//        for (Example e : le) {
//            e.setSimhash(getSimHash(e.getDescription()));
////            System.out.println(e.getSimhash());
//            Long sh2 = e.getSimhash();
//            int dis = simHash.hamming(sh1, sh2);
//            if (simHash.isSimilar(dis)) {
//                for (int i = 0; i < similarExampleNumber; i++) {
//                    if (mSimilarExamples.get(i) == null) {
//                        mSimilarExamples.put(i, new Tuple<>(e, dis));
//                        break;
//                    } else if (dis < mSimilarExamples.get(i).dis) {
//                        for (int j = similarExampleNumber - 1; j > i; j--) {
//                            mSimilarExamples.put(j, mSimilarExamples.get(j - 1));
//                        }
//                        mSimilarExamples.put(i, new Tuple<>(e, dis));
//                        break;
//                    }
//                }
//            }
//        }
//
//        for (int i = 0; i < similarExampleNumber; i++) {
//            if (mSimilarExamples.get(i) != null) lsimilarE.add(mSimilarExamples.get(i).e);
//        }
//        if (lsimilarE.size() != 0) return lsimilarE;
//        else return null;
//    }
//}
=======
package com.nqff.drms.algorithm;

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
            tfidfAnalyzer = new TFIDFAnalyzer(stopWordsSet, idfMap);
        }
        if (simHash == null) {
            simHash = new SimHash(stopWordsSet);
        }
    }

    public long getSimHash(String content) {
        return SimHash.calSimHash(content);
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
>>>>>>> 93bec59fcb30e61920b3fe7eaa6be674f91104ca
