package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.KeywordDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KeywordServiceImpl extends ServiceImpl<KeywordDao,Keyword> implements KeywordService {
    @Autowired
    private KeywordDao keywordDao;

    @Override
    public void InsertKeyword(Keyword keyword) {
        keywordDao.insert(keyword);
    }

    @Override
    public Keyword selectKeywordByName(String name) {
        LambdaQueryWrapper<Keyword> wrapper = new LambdaQueryWrapper<Keyword>();
        wrapper.eq(Keyword::getName,name);
        List<Keyword> keywords = keywordDao.selectList(wrapper);
        if(keywords == null)
            return null;
        return keywords.get(0);
    }
}
