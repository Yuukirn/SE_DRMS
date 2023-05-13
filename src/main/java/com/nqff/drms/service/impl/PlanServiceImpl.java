package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.KeywordDao;
import com.nqff.drms.dao.PlanDao;
import com.nqff.drms.dao.SubprojectKeywordRelationDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.SubprojectKeywordRelation;
import com.nqff.drms.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanDao, Plan> implements PlanService {
    @Autowired
    private PlanDao planDao;

    @Override
    public void insertPlan(Plan plan) {
        planDao.insert(plan);
    }

    @Override
    public boolean isPlanExist(int subProjectId) {
        LambdaQueryWrapper<Plan> wrapper = new LambdaQueryWrapper<Plan>();
        wrapper.eq(Plan::getSubprojectId,subProjectId);
        return planDao.selectList(wrapper).size() > 0;
    }

    @Override
    public List<Plan> selectPlansByName(String name) {
        LambdaQueryWrapper<Plan> wrapper = new LambdaQueryWrapper<Plan>();
        wrapper.like(Plan::getName,name);
        return planDao.selectList(wrapper);
    }

    @Override
    public Plan selectPlanById(int id) {
        return planDao.selectPlanById(id);
    }

}
