package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanDao extends BaseMapper<Plan> {
    @Select("select * from `plan` where id = #{id} and deleted = 0")
Plan selectPlanById(int id);

    @Select("select * from `plan` where subproject_id = #{subproject_id} and deleted = 0")
    List<Plan> selectBySubprojectId(int subproject_id);
}