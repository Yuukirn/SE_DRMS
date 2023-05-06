package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
    @Select("select * from `category` where project_id = #{project_id}")
    Category selectByProjectId(int project_id);

    @Select("select * from `category` where user_id = #{user_id} and project_id = #{project_id}")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "plans",javaType = List.class,
            many = @Many(select = "com.nqff.drms.dao.ExampleDao.selectByCategoryId"))
    })
    List<Category> selectAllCategoryByProjectId(int project_id);
}
