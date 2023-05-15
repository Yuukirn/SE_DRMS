package com.nqff.drms.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.service.ProjectService;
import com.nqff.drms.service.SubprojectService;
import jakarta.annotation.PostConstruct;
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
    static private ProjectService staticProjectService;
    @Autowired
    private SubprojectService subprojectService;

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


    private static boolean isSimilar(Subproject sp1, Subproject sp2) {
        return true;
    }

    @PostConstruct
    public void init() {
        simHash = this;
        simHash.subprojectService = this.subprojectService;
    }

//    /**
//     * 获取相似子项目
//     *
//     * @param sp
//     * @return
//     */
//    public List<Subproject> getSimilarSubprojects(Subproject sp) {
//        sp.setSimHash(calSimHash(sp.getDescription()));
//        List<Subproject> lSimSp = new ArrayList<>();
//        List<Subproject> lSp = simHash.subprojectService.list();
//        for (Subproject sp2 : lSp) {
//            sp2.setSimHash(calSimHash(sp2.getDescription()));
//            if (sp2 != sp && isSimilar(sp2.getSimHash(), sp.getSimHash())) {
//                lSimSp.add(sp2);
//            }
//        }
//        System.out.println(lSimSp);
//        return lSimSp;
//    }

    /**
     * 获取与该子项目相似的子项目下的方案
     *
     * @param subprojects 相似子项目列表
     * @param sp          子项目
     * @param n           获取方案数
     * @return 方案列表
     */
    public List<Plan> getPlansFromProjects(List<Subproject> subprojects, Subproject sp, int n) {
        Map<Plan, Integer> planMap = new HashMap<>();
        List<Plan> plans = new ArrayList<>();
        for (Subproject subproject : subprojects) {
            if (subproject.getPlan() == null) continue;
            int minHamming = Integer.MAX_VALUE;

            planMap.put(subproject.getPlan(), minHamming);
        }

        List<Map.Entry<Plan, Integer>> entryList = new ArrayList<Map.Entry<Plan, Integer>>(planMap.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Plan, Integer>>() {
            @Override
            public int compare(Map.Entry<Plan, Integer> o1, Map.Entry<Plan, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        if (planMap.size() > n) {
            int num = planMap.size() - n;
            for (int i = 0; i < num; i++) {
                entryList.remove(n);
            }
        }
        for (Map.Entry<Plan, Integer> entry : entryList) {
            plans.add(entry.getKey());
        }
        System.out.println(plans);
        return plans;
    }
}
