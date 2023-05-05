package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.PlanDao;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanDao, Plan> implements PlanService {
    @Autowired
    private PlanDao planDao;

    @Override
    public void insertPlan(Plan plan) {
        planDao.insert(plan);
    }
}
