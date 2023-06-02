package com.nqff.drms.algorithm;

import org.apache.commons.lang3.tuple.MutablePair;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;

public class TextGenerator {
    public static final int MAX_LENGTH = 1024;
    public static String generate(List<Tuple2<Map<Integer, List<MutablePair<String, Double>>>, Double>> contentsSentencesWeightList) {
        StringBuilder res = new StringBuilder();
        double parNum = 0; // 平均段落数
        Map<Integer, Double> cwl = new HashMap<>(); // 每一个文章与器权重的键值对
        int n = 0;  // 文章数
        /* 计算平均段落数 */
        for (Tuple2<Map<Integer, List<MutablePair<String, Double>>>, Double> tuple : contentsSentencesWeightList) {
            cwl.put(++n, tuple.getT2());
            parNum += tuple.getT1().size() * tuple.getT2();
        }
        /* 依据平均段落数生成新段 */
        Map<Integer, Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>>> l = generateNewPar(contentsSentencesWeightList, (int) parNum);

        /* 计算每一段的平均句数 */
        Map<Integer, Double> centNumPerPar = new HashMap<>();
        for (int i = 1; i <= (int) parNum; i++) centNumPerPar.put(i, 0d);
        for (Map.Entry<Integer, Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>>> c : l.entrySet())
            for (Map.Entry<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>> entry : c.getValue().entrySet())
                centNumPerPar.put(entry.getKey(), centNumPerPar.get(entry.getKey()) + entry.getValue().size() * cwl.get(c.getKey()));

        List<Map<Integer, String>> ml = new ArrayList<>();  // 每一段中获取的文字
        for (int i = 1; i <= n; i++) {
            Map<Integer, String> sbm = new HashMap<>();
            /* 取出当前文章 */
            Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>> content = l.get(i);
            for (int j = 1; j <= content.size(); j++) {
                StringBuilder sb = new StringBuilder();
                /* 取出当前段 */
                List<Tuple2<Integer, MutablePair<String, Double>>> paragraph = content.get(j);
                List<Tuple2<Integer, String>> cl = new ArrayList<>();
                for (int k = 0; k < centNumPerPar.get(j) * cwl.get(i); k++) {
                    if(k<paragraph.size()) {
                        cl.add(Tuples.of(paragraph.get(k).getT1(), paragraph.get(k).getT2().getLeft()));
                    }
                }
                Collections.sort(cl, new Comparator<Tuple2<Integer, String>>() {
                    @Override
                    public int compare(Tuple2<Integer, String> o1, Tuple2<Integer, String> o2) {
                        return -Double.compare(o1.getT1(), o2.getT1());
                    }
                });
                for (Tuple2<Integer, String> t : cl) {
                    sb.append(t.getT2());
                }
                sbm.put(j, sb.toString());
//                if (j != content.size()) sb.append('\n');
            }
            ml.add(sbm);
        }
        for (int i = 1; i <= (int) parNum; i++) {
            for (Map<Integer, String> m : ml) {
                String sentence = m.get(i);
                if(sentence == null)
                    continue;
                if(res.length() + sentence.length() > MAX_LENGTH){
                    return res.toString();
                }
                if (i <= m.size()) {
                    res.append(sentence);
                }
            }
            if (i != (int) parNum) res.append('\n');
        }
        return res.toString();
    }

    /**
     * 调整成新段落数
     */
    private static Map<Integer, Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>>> generateNewPar(List<Tuple2<Map<Integer, List<MutablePair<String, Double>>>, Double>> contentsSentencesWeightList, int parNum) {
        Map<Integer, Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>>> newCs = new HashMap<>();
        int n = 0;
        for (Tuple2<Map<Integer, List<MutablePair<String, Double>>>, Double> tuple : contentsSentencesWeightList) {
            n++;
            Map<Integer, List<MutablePair<String, Double>>> m = tuple.getT1();
            Map<Integer, List<Tuple2<Integer, MutablePair<String, Double>>>> newM = new HashMap<>();
            if (m.size() <= parNum) {
                for (int i = 1; i <= m.size(); i++) {
                    int cn = 0;
                    List<Tuple2<Integer, MutablePair<String, Double>>> l = new ArrayList<>();
                    for (MutablePair<String, Double> pair : m.get(i)) {
                        cn++;
                        l.add(Tuples.of(cn, pair));
                    }
                    newM.put(i, sortByWeight(l));
                }
            } else {
                int p = parNum;
                int pn = 0;
                while (p-- > 0) {
                    int newN = parNum - p;    // 当前段落
                    List<Tuple2<Integer, MutablePair<String, Double>>> l = new ArrayList<>();
                    int cn = 0;
                    for (int i = 0; (i < m.size() / parNum) || ((i == m.size() / parNum) && (p < m.size() % parNum)); i++) {
                        pn++;
                        for (MutablePair<String, Double> pair : m.get(pn)) {
                            cn++;
                            l.add(Tuples.of(cn, pair));
                        }
                    }
                    newM.put(newN, sortByWeight(l));
                }
            }
            newCs.put(n, newM);
        }
        return newCs;
    }

    /**
     * 将句子按照权重排序
     */
    private static List<Tuple2<Integer, MutablePair<String, Double>>> sortByWeight(List<Tuple2<Integer, MutablePair<String, Double>>> l) {
        Collections.sort(l, new Comparator<>() {
            @Override
            public int compare(Tuple2<Integer, MutablePair<String, Double>> o1, Tuple2<Integer, MutablePair<String, Double>> o2) {
                return -Double.compare(o1.getT2().getRight(), o2.getT2().getRight());
            }
        });
        return l;
    }

//    private static List
}
