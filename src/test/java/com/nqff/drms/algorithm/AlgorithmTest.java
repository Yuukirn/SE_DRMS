package com.nqff.drms.algorithm;

import org.junit.jupiter.api.Test;

class AlgorithmTest {
    @Test
    void createAlgorithm() {
        Algorithm algorithm = new Algorithm();
        String content = "一句话介绍：墨刀是一款打通产设研团队，实现原型，设计，流程，思维导图一体化的在线协同工具。\n" +
                "\n" +
                "展开说说：墨刀隶属于万兴科技集团， 公司自成立以来一直致力于产品设计在线协同办公软件的研究与开发。";
        System.out.println(algorithm.getKeyWord(content));
        System.out.println(algorithm.getKeyWord(content, 3));
    }

}