package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Plan;

import java.util.List;

public interface PlanService extends IService<Plan> {
    void insertPlan(Plan plan);
    boolean isPlanExist(int subProjectId);
    List<Plan> selectPlansByName(String name);
    List<Plan> selectPlanByKeywordId(int keyword_id);
    Plan selectById(int id);
    void updatePlanById(Plan plan);
}