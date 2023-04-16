package com.nqff.drms.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

//    public Result(int code, String msg, T data) {
//        this.code = code;
//        this.msg = msg;
//        this.data = data;
//    }

    public static <T> Result SUCCESS(T data) {
        return new Result(200, "ok", data);
    }

    public static <T> Result FAIL(String msg, T data) {
        return new Result(200, msg, data);
    }
}
