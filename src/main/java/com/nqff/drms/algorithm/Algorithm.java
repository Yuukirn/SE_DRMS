package com.nqff.drms.algorithm;


import com.fasterxml.jackson.annotation.JacksonInject;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.ExampleService;
import com.nqff.drms.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Algorithm {
    private int similarExampleNumber = 3;
    @Autowired
    private ExampleService exampleService;
    private final SimHash simHash = new SimHash(4, 3);
//    private Murmur3 murmur3;

//    @Operation (summary = "根据方案描述获得相似案例", security = {@SecurityRequirement(name = "Authorization")})
    public Result getSimilarExample(String description) {
        List<Example> examples = selectSimilarExampleByPlanDescription(description);
        if (examples == null) {
            return Result.FAIL("not found similar example", null);
        }
        return Result.SUCCESS(examples);
    }

    private List<Example> selectSimilarExampleByPlanDescription(String description) {
        class Tuple<Example, Integer> {
            Example e;
            Integer dis;
            public Tuple(Example e, Integer dis) {
                this.e = e;
                this.dis = dis;
            }
        }

        long sh1 = simHash.calSimHash(description);
        List<Example> le = exampleService.list();
        List<Example> lsimilarE = new ArrayList<>();
        Map<Integer, Tuple<Example, Integer>> mSimilarExamples = new HashMap<>();

        for (int i = 0; i < similarExampleNumber; i++) {
            mSimilarExamples.put(i, null);
        }

        for (Example e : le) {
            Long sh2 = e.getSimHash();
            int dis = simHash.hamming(sh1, sh2);
            if (simHash.isSimilar(dis)) {
                for (int i = 0; i < similarExampleNumber; i++) {
                    if (mSimilarExamples.get(i) != null) {
                        mSimilarExamples.put(i, new Tuple<>(e, dis));
                    } else if (dis > mSimilarExamples.get(i).dis) {
                        mSimilarExamples.put(i, new Tuple<>(e, dis));
                    }
                }
            }
        }

        for (int i = 0; i < similarExampleNumber; i++) {
            lsimilarE.add(mSimilarExamples.get(i).e);
        }
        return lsimilarE;
    }
}
