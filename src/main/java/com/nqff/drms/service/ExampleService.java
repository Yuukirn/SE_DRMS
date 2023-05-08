package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Example;

import java.util.List;

public interface ExampleService extends IService<Example> {
    void insertExample(Example example);

    List<Example> selectExampleByName(String name);
}
