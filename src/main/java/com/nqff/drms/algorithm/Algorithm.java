package com.nqff.drms.algorithm;

import com.nqff.drms.dao.ProjectDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Project;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.service.SubprojectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
    static private final int KeyWordNum = 5;
    static private final int SimilarProjectNum = 3;
    static protected double idfMedian;
    static private Algorithm algorithm;
    static private HashMap<String, Double> idfMap;
    static private HashSet<String> stopWordsSet;
    static private TFIDFAnalyzer tfidfAnalyzer;
    static private SimHash simHash;

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private SubprojectService subprojectService;

    /**
     * 加载停用词表
     *
     * @param set
     * @param in
     */
    private static void loadStopWords(Set<String> set, InputStream in) {
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
    private static void loadIDFMap(Map<String, Double> map, InputStream in) {
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
<<<<<<< HEAD
}
=======

    /**
     * 获取文本关键字
     *
     * @param content
     * @return 默认返回权重最大的5个关键字，若不足5个则返回全部关键字
     */
    public static List<Keyword> getKeyWord(String content) {
        return tfidfAnalyzer.getKeyWord(content, KeyWordNum);
    }

    @PostConstruct
    private void init() {
        algorithm = this;
        algorithm.subprojectService = this.subprojectService;
        algorithm.projectDao = this.projectDao;
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
        for (Project p : projectDao.selectList(null)) {

        }
        for (Subproject sp : subprojectService.list()) {
            if (sp.getDescription() == null) continue;
            sp.setKeywords(getKeyWord(sp.getDescription()));
            sp.setSimHash(getSimHash(sp.getDescription()));
            subprojectService.updateSubprojectById(sp);
        }
    }

    public long getSimHash(String content) {
        return SimHash.calSimHash(content);
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
     * 根据子项目所属的项目查询相似项目下的方案
     *
     * @param id 子项目id
     * @return 与项目相似的方案列表
     */
    public List<Plan> getSimilarPlans(int id) {
        return getSimilarPlans(id, SimilarProjectNum);
    }

    /**
     * 根据子项目所属的项目查询相似项目下的方案
     *
     * @param id 子项目id
     * @param n  相似方案数
     * @return 与项目相似的方案列表
     */
    public List<Plan> getSimilarPlans(int id, int n) {
        Subproject sp = subprojectService.selectById(id);
        List<Subproject> lsp = subprojectService.list();
        List<Plan> plans = simHash.getPlansFromProjects(lsp, sp, SimilarProjectNum);
        return plans;
    }
}
>>>>>>> 6178ddc1f541d362316e2803b32be1b6719e7710
