package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.KeywordDao;
import com.nqff.drms.dao.PlanDao;
import com.nqff.drms.dao.PlanKeywordRelationDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.PlanKeywordRelation;
import com.nqff.drms.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanServiceImpl extends ServiceImpl<PlanDao, Plan> implements PlanService {
    @Autowired
    private PlanDao planDao;
    @Autowired
    private PlanKeywordRelationDao planKeywordRelationDao;
    @Autowired
    private KeywordDao keywordDao;

    @Override
    public void insertPlan(Plan plan) {
        planDao.insert(plan);
        int plan_id = plan.getId();
        for(Keyword keyword : plan.getKeywords()){
            PlanKeywordRelation relation = new PlanKeywordRelation();
            relation.setPlanId(plan_id);
            relation.setKeywordId(keyword.getId());
            planKeywordRelationDao.insert(relation);
        }
    }

    @Override
    public boolean isPlanExist(int subProjectId) {
        LambdaQueryWrapper<Plan> wrapper = new LambdaQueryWrapper<Plan>();
        wrapper.eq(Plan::getSubProjectId,subProjectId);
        return planDao.selectList(wrapper).size() > 0;
    }

    @Override
    public List<Plan> selectPlansByName(String name) {
        LambdaQueryWrapper<Plan> wrapper = new LambdaQueryWrapper<Plan>();
        wrapper.like(Plan::getName,name);
        return planDao.selectList(wrapper);
    }

    @Override
    public List<Plan> selectPlanByKeywordId(int keyword_id) {
        List<PlanKeywordRelation> relations = planKeywordRelationDao.selectByKeywordId(keyword_id);
        List<Plan> plans = new ArrayList<Plan>();
        for(PlanKeywordRelation relation : relations){
            plans.add(planDao.selectById(relation.getPlanId()));
        }
        return plans;
    }


    public Plan selectById(int id){
        Plan plan = planDao.selectById(id);
        List<PlanKeywordRelation> relations = planKeywordRelationDao.selectByPlanId(plan.getId());
        List<Keyword> keywords = new ArrayList<Keyword>();
        for(PlanKeywordRelation relation : relations){
            keywords.add(keywordDao.selectById(relation.getKeywordId()));
        }
        plan.setKeywords(keywords);
        return plan;
    }

    @Override
    public void updatePlanById(Plan plan) {
        updateById(plan);
        int plan_id = plan.getId();
        for(Keyword keyword : plan.getKeywords()){
            PlanKeywordRelation relation = new PlanKeywordRelation();
            relation.setPlanId(plan_id);
            relation.setKeywordId(keyword.getId());
            planKeywordRelationDao.insert(relation);
        }
    }

}
