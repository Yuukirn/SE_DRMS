package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Plan;

public interface PlanService extends IService<Plan> {
    void insertPlan(Plan plan);
}