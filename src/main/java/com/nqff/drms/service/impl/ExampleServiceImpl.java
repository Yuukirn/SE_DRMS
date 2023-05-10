package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.ExampleDao;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleServiceImpl extends ServiceImpl<ExampleDao, Example> implements ExampleService {
    @Autowired
    private ExampleDao exampleDao;

    @Override
    public void insertExample(Example example) {
        exampleDao.insert(example);
    }

    @Override
    public List<Example> selectExampleByName(String name) {
        LambdaQueryWrapper<Example> lqw = new LambdaQueryWrapper<Example>();
        lqw.like(Example::getName, name);
        return exampleDao.selectList(lqw);
    }

    @Override
    public Example selectExampleById(int id) {
        return exampleDao.selectExampleById(id);
    }
}
