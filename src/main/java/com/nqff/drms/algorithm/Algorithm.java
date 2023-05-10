package com.nqff.drms.algorithm;


import com.nqff.drms.dao.ExampleDao;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/algorithm")
@Tag(name = "算法")
public class Algorithm {
    private final SimHash simHash = new SimHash(4, 20);
    private int similarExampleNumber = 3;
    @Autowired
    private ExampleDao exampleDao;
//    private Murmur3 murmur3;

    @Operation(summary = "查询相似案例", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping()
    public Result getSimilarExample(@RequestBody String description) {
        List<Example> examples = selectSimilarExampleByPlanDescription(description);
        if (examples == null) {
            return Result.FAIL("not found similar example", null);
        }
        return Result.SUCCESS(examples);
    }


    public long getSimHash(String content) {
        return simHash.calSimHash(content);
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
//        System.out.println(sh1);
//        List<Example> le = exampleService.list();
        List<Example> le = exampleDao.selectList(null);
        List<Example> lsimilarE = new ArrayList<>();
        Map<Integer, Tuple<Example, Integer>> mSimilarExamples = new HashMap<>();

        for (int i = 0; i < similarExampleNumber; i++) {
            mSimilarExamples.put(i, null);
        }

        for (Example e : le) {
            e.setSimhash(getSimHash(e.getDescription()));
//            System.out.println(e.getSimhash());
            Long sh2 = e.getSimhash();
            int dis = simHash.hamming(sh1, sh2);
            if (simHash.isSimilar(dis)) {
                for (int i = 0; i < similarExampleNumber; i++) {
                    if (mSimilarExamples.get(i) == null) {
                        mSimilarExamples.put(i, new Tuple<>(e, dis));
                        break;
                    } else if (dis > mSimilarExamples.get(i).dis) {
                        mSimilarExamples.put(i, new Tuple<>(e, dis));
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < similarExampleNumber; i++) {
            if (mSimilarExamples.get(i) != null) lsimilarE.add(mSimilarExamples.get(i).e);

        }
        if (lsimilarE.size() != 0) return lsimilarE;
        else return null;
    }
}
