package com.nqff.drms.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.nqff.drms.dao.SubprojectKeywordRelationDao;
import com.nqff.drms.pojo.Document;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.pojo.SubprojectKeywordRelation;
import com.nqff.drms.service.DocumentService;
import com.nqff.drms.service.PlanService;
import com.nqff.drms.service.SubprojectService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.*;

@Component
public class SimHash {
    private static final int bitNum = 64;
    static private final int fracCount = 4;
    static private final int hammingThresh = 20;
    private static SimHash simHash;
    static private HashSet<String> stopWordsSet;
    static private JiebaSegmenter segmenter = new JiebaSegmenter();
    @Autowired
    private SubprojectService subprojectService;
    @Autowired
    private PlanService planService;
    @Autowired
    private SubprojectKeywordRelationDao subprojectKeywordRelationDao;
    @Autowired
    private DocumentService documentService;

    public SimHash(HashSet<String> set) {
        if (stopWordsSet == null) stopWordsSet = set;
    }

    public static boolean isSimilar(long sh1, long sh2) {
        return hamming(sh1, sh2) < hammingThresh;
    }

    public static boolean isSimilar(int dis) {
        return dis < hammingThresh;
    }

    /**
     * 计算文本的simHash值
     *
     * @param content content
     * @return 文本simHash值
     */
    public static Long calSimHash(String content) {
        String filterContent = content.trim().replaceAll("\\p{Punct}|\\p{Space}", "");
        List<String> segments = segmenter.sentenceProcess(filterContent);
        Integer[] weight = new Integer[bitNum];
        Arrays.fill(weight, 0);
        Iterator<String> iterator = segments.listIterator();
        String segment;
        while (iterator.hasNext()) {
            segment = iterator.next();
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
                iterator.remove();
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

    public static int Distance(String s1, String s2) {
        return hamming(calSimHash(s1), calSimHash(s2));
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

    private static double Distance(Plan p, Subproject sp) {
        int SameKeyWordNum = 0;
        List<SubprojectKeywordRelation> subProRelationList = simHash.subprojectKeywordRelationDao.selectBySubprojectId(sp.getId());
        List<SubprojectKeywordRelation> planRelationList = simHash.subprojectKeywordRelationDao.selectBySubprojectId(p.getSubprojectId());
        for (SubprojectKeywordRelation relation : subProRelationList) {
            /* 若该关键字为子项目关键字 */
            for (SubprojectKeywordRelation relation2 : planRelationList) {
                if (relation2.getDeleted() == 0 && relation2.getKeywordId() == relation.getKeywordId()) {
                    SameKeyWordNum++;
                    continue;
                }
            }
        }
        double dis = Integer.MAX_VALUE;
        String desc1 = p.getDescription();
        String desc2 = sp.getDescription();
        if (desc1 == desc2) {
            dis = 0;
        } else if (desc1 == null || desc2 == null) {
            dis = bitNum;
        } else {
            dis = hamming(calSimHash(p.getDescription()), calSimHash(sp.getDescription()));
        }
        if (dis != 0) dis *= (1 - Math.log10(SameKeyWordNum + 1));
//        System.out.println("plan id = " + p.getName() + "\n\tdis = " + dis);
        return dis;
    }

    @PostConstruct
    public void init() {
        simHash = this;
        simHash.subprojectService = this.subprojectService;
        simHash.planService = this.planService;
        simHash.subprojectKeywordRelationDao = this.subprojectKeywordRelationDao;
        simHash.documentService = this.documentService;
    }

    /**
     * 获取与该子项目相似的子项目下的方案
     *
     * @param sp 子项目
     * @param n  获取方案数
     * @return 方案列表
     */
    public List<Plan> getPlansSimilarToSubproject(Subproject sp, int n) {
        Map<Plan, Double> planMap = new HashMap<>();
        List<Plan> plans = new ArrayList<>();
        for (Plan plan : simHash.planService.list()) {
            if (plan.getUserId() == sp.getUserId()) {
                planMap.put(plan, Distance(plan, sp));
            }
        }

        List<Map.Entry<Plan, Double>> entryList = new ArrayList<Map.Entry<Plan, Double>>(planMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Plan, Double>>() {
            @Override
            public int compare(Map.Entry<Plan, Double> o1, Map.Entry<Plan, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        if (planMap.size() > n) {
            int num = planMap.size() - n;
            for (int i = 0; i < num; i++) {
                entryList.remove(n);
            }
        }
        for (Map.Entry<Plan, Double> entry : entryList) {
            plans.add(entry.getKey());
        }
//        System.out.println(plans);
        return plans;
    }

    public List<Document> getDocuments(int subProId) {
        List<Document> documentList = new ArrayList<>();
        for (Document document : documentService.list()) {
            if (document.getSubprojectId() == subProId) {
                documentList.add(document);
            }
        }
        if (documentList.size() == 0) return null;
        return documentList;
    }

    public List<Pair<String, Double>> getPlanWeights(List<Plan> planList, int id) {
        List<Pair<String, Double>> pwList = null;
        if (planList != null) {
            pwList = new ArrayList<>();
            double simSum = 0;
            for (Plan plan : planList) {
                double dis = Distance(plan, simHash.subprojectService.getById(id));
                double similarity = bitNum - dis;
                simSum += similarity;
                pwList.add(new MutablePair<>(plan.getDescription(), similarity));
            }
            for (Pair<String, Double> pw : pwList) {
                pw.setValue(pw.getValue() / simSum);
//                System.out.println(pw);
            }
        }
        return pwList;
    }

    public List<Pair<String, Double>> getDocumentContent(int subProId) {
        List<Pair<String, Double>> dList = new ArrayList<>();
        List<Document> documentList = simHash.documentService.selectDocumentsBySubprojectId(subProId);
        for (Document document : documentList) {
            dList.add(new MutablePair<>(DocumentReader.readDocument(document), 1d / documentList.size()));
        }
        if (dList.size() == 0) return null;
        return dList;
    }
}
