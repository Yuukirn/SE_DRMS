package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.PlanKeywordRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlanKeywordRelationDao extends BaseMapper<PlanKeywordRelation> {
    @Select("select * from `plan_keyword_relation` where plan_id = #{plan_id} and deleted = 0")
    List<PlanKeywordRelation> selectByPlanId(int plan_id);
    @Select("select * from `plan_keyword_relation` where keyword_id = #{keyword_id} and deleted = 0")
    List<PlanKeywordRelation> selectByKeywordId(int keyword_id);
}
