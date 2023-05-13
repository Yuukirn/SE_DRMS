package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.sub_project;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubProjectDao extends BaseMapper<sub_project> {
    @Select("select * from `sub_project` where project_id = #{project_id}")
    sub_project selectByProjectId(int project_id);

    @Select("select * from `sub_project` where project_id = #{project_id} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "examples",javaType = List.class,
            many = @Many(select = "com.nqff.drms.dao.ExampleDao.selectByCategoryId"))
    })
    List<sub_project> selectAllSubProjectByProjectId(int project_id);

    @Select("select * from `sub_project` where `name` like #{name} and project_id = #{pid} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "examples",javaType = List.class,
                    many = @Many(select = "com.nqff.drms.dao.ExampleDao.selectByCategoryId"))
    })
    List<sub_project> selectSubProjectByNameAndPid(int pid, String name);
}
