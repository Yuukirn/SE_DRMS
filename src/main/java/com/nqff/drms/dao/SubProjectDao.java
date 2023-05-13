package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.SubProject;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubProjectDao extends BaseMapper<SubProject> {
    @Select("select * from `sub_project` where project_id = #{project_id}")
    SubProject selectByProjectId(int project_id);

    @Select("select * from `sub_project` where project_id = #{project_id} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "plan",javaType = Plan.class,
            one = @One(select = "com.nqff.drms.dao.PlanDao.selectBySubProjectId"))
    })
    List<SubProject> selectAllSubProjectByProjectId(int project_id);

    @Select("select * from `sub_project` where `name` like #{name} and project_id = #{pid} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "plan",javaType = Plan.class,
                    one = @One(select = "com.nqff.drms.dao.PlanDao.selectBySubProjectId"))
    })
    List<SubProject> selectSubProjectByNameAndPid(int pid, String name);
}
