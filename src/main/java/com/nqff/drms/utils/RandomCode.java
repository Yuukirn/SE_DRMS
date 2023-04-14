package com.nqff.drms.utils;

import java.util.Random;

public class RandomCode {
    public static String GenerateRandomCode() {
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; ++i) {
            res.append(random.nextInt(10));
        }
        return res.toString();
    }
}
