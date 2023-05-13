package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Plan;

import java.util.List;

public interface PlanService extends IService<Plan> {
    void insertPlan(Plan plan);
    boolean isPlanExist(int subProjectId);
    List<Plan> selectPlansByName(String name);
    Plan selectPlanById(int id);
}