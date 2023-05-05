package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.ExampleDao;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl extends ServiceImpl<ExampleDao, Example> implements ExampleService {
    @Autowired
    private ExampleDao exampleDao;

    @Override
    public void insertExample(Example example) {
        exampleDao.insert(example);
    }
}
