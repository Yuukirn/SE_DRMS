package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Keyword;

public interface KeywordService extends IService<Keyword> {
    void InsertKeyword(Keyword keyword);
    Keyword selectKeywordByName(String name);
}
