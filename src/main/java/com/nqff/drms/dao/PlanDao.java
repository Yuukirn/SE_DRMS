package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Plan;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlanDao extends BaseMapper<Plan> {
    @Select("select * from `plan` where id = #{id} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "documents",javaType = List.class,
                    many = @Many(select = "com.nqff.drms.dao.DocumentDao.selectByPlanId"))
    })
Plan selectPlanById(int id);

    @Select("select * from `plan` where subproject_id = #{subproject_id} and deleted = 0")
    Plan selectBySubprojectId(int subproject_id);
}