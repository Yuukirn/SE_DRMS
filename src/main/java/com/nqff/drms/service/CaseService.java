package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Case;

public interface CaseService extends IService<Case> {
    void insertCase(Case _case);
}