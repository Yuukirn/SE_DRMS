package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubprojectDao extends BaseMapper<Subproject> {

    @Select("select * from `subproject` where project_id = #{project_id} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "plan",javaType = Plan.class,
            one = @One(select = "com.nqff.drms.dao.PlanDao.selectBySubprojectId")),

    })
    List<Subproject> selectAllSubProjectByProjectId(int project_id);

    @Select("select * from `subproject` where `name` like #{name} and project_id = #{pid} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "plan",javaType = Plan.class,
                    one = @One(select = "com.nqff.drms.dao.PlanDao.selectBySubprojectId"))
    })
    List<Subproject> selectSubProjectByNameAndProjectId(int pid, String name);
}
