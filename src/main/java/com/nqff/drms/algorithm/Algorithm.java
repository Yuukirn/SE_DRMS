package com.nqff.drms.algorithm;

import com.nqff.drms.dao.ProjectDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.service.SubprojectService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;


@Service
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
     */
    private static void loadStopWords(Set<String> set, InputStream in) {
        BufferedReader bufr;
        try {
            bufr = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
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


    /**
     * 获取文本关键字
     *
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
//        for (Project p : projectDao.selectList(null)) {
//
//        }
//        for (Subproject sp : subprojectService.list()) {
//            if (sp.getDescription() == null) continue;
//            sp.setKeywords(getKeyWord(sp.getDescription()));
//            sp.setSimHash(getSimHash(sp.getDescription()));
//            subprojectService.updateSubprojectById(sp);
//            subprojectService.selectById(sp.getId());
//        }
    }

    public long getSimHash(String content) {
        return SimHash.calSimHash(content);
    }

    /**
     * 获取文本关键字
     *
     * @return 返回权重最大的n个关键字，若不足n个则返回全部关键字
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
        List<Plan> plans = simHash.getPlansSimilarToSubproject(sp, SimilarProjectNum);
        return plans;
    }

    /**
     * 遍历各个文档的所有句子，每个句子按照以下两个关系计算权重 <br/>
     * 1.   文档与该子项目的关系 <br/>
     * （1）是从相似的子项目种得到方案文档 —— 按照子项目相似度sim 计算权重 w1.1 = sim / ∑sim <br/>
     * （2）子项目中用户上传的资料  —— 固定权重 w1.2 <br/>
     * 2.   这个句子与简介的相似度 w2 <br/>
     * <br/>
     * parNum     = w1 * curParNum              生成文本的段落数 <br/>
     * parCentNum = w1 * curParCentNum          生成文本的各个段落的句数 <br/>
     * <br/>
     * curParGetCentNum = w1 * parCentNum       从该段落中获取的句数 <br/>
     * {w2} from curParCents.w2 <br/>
     * curCent = getMax{w2} <br/>
     *
     * @param subproject_id 需要生成方案的子项目id
     * @return
     */
    public Plan generateNewPlan(List<Plan> planList, int subproject_id, int user_id) {
//        System.out.println(planList);
        Plan newPlan = new Plan();
        String name = subprojectService.getById(subproject_id).getName() + "方案";
        String description;

        List planDescWeightList = simHash.getPlanWeights(planList, subproject_id);
        List docContextList = simHash.getDocumentContent(subproject_id);
        String str1 = createNewContent(planDescWeightList, subproject_id, user_id);
        String str2 = createNewContent(docContextList, subproject_id, user_id);
        if (str1.equals("")) {
            description = str2;
        } else {
            if (str2.equals("")) {
                description = str1;
            } else {
                List strList = new ArrayList<>();
                strList.add(new ImmutablePair<>(str1, 0.5));
                strList.add(new ImmutablePair<>(str2, 0.5));
                description = createNewContent(strList, subproject_id, user_id);
            }
        }

        newPlan.setName(name);
        newPlan.setDescription(description == null ? "" : description);
        newPlan.setSubprojectId(subproject_id);
        newPlan.setUserId(user_id);
        return newPlan;
    }


    private String createNewContent(List<MutablePair<String, Double>> cwList, int subproject_id, int user_id) {
        String desc = algorithm.subprojectService.selectById(subproject_id).getDescription();
        /* 每个文本每段每句权重 */
        List<Tuple2<Map<Integer, List<MutablePair<String, Double>>>, Double>> contentsSentencesWeightListToSubproject = new ArrayList<>();

        if(cwList != null){
            for (Pair<String, Double> cw : cwList) {
                /* 当前文本的每段每句 */
                Map<Integer, List<MutablePair<String, Double>>> parSentsMap = new HashMap<>();
                int parN = 1;   // 当前段落数
                int disSum = 0;
                String content = cw.getLeft();
                double w = cw.getRight();   // 当前文本总体权重
                CharacterIterator characterIterator = new StringCharacterIterator(content);
                do {
                    char c = characterIterator.current();
                    while (c == '\n') c = characterIterator.next();
                    if (c != '\uFFFF') parSentsMap.put(parN++, new ArrayList<>());
                    while (c != '\n' && c != '\uFFFF') {
                        StringBuilder sb = new StringBuilder();
                        while (c != '.' && c != '。' && c != '\n' && c != '\0' && c != '\uFFFF' && c != '\r' && c != '?' && c != '!' && c != '？' && c != '！') {
                            sb.append(c);
                            c = characterIterator.next();
                        }
                        if (c == '.') {
                            c = characterIterator.next();
                            while (c == '.') {
                                sb.append(c);
                                c = characterIterator.next();
                            }
                            c = characterIterator.previous();
                        }
                        if (c == '\r') c = characterIterator.next();
                        if (!(c == '\uFFFF' || c == '\n')) sb.append(c);
                        if (c != '\uFFFF' && c != '\n') c = characterIterator.next();
                        String str = sb.toString();
                        if (!str.equals("") && str.charAt(0) == '\0') continue;
                        int d = simHash.Distance(str, desc);
                        disSum += d;
                        parSentsMap.get(parN - 1).add(new MutablePair<>(str, (double) d));
                    }
                } while (characterIterator.current() != '\uFFFF');

                for (List<MutablePair<String, Double>> pairList : parSentsMap.values()) {
                    for (MutablePair<String, Double> pair : pairList) {
                        pair.setValue(w * pair.getValue() / disSum);
                    }
                }
//            System.out.println(parSentsMap);
                contentsSentencesWeightListToSubproject.add(Tuples.of(parSentsMap, w));
            }
        }

//        System.out.println(contentsSentencesWeightListToSubproject);
        return TextGenerator.generate(contentsSentencesWeightListToSubproject);
    }
}