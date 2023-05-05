package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
    @Select("select * from `category` where project_id = #{project_id} and user_id = #{user_id}")
    Category selectByUserIdAndProjectId(int user_id,int project_id);

    @Select("select * from `category` where user_id = #{user_id} and project_id = #{project_id}")
    @Results({
            @Result(column = "{user_id = user_id,project_id = project_id,category_id = id}",property = "cases",javaType = List.class,
            many = @Many(select = "com.nqff.drms.dao.CaseDao.selectByUserIdAndProjectIdAndCategoryId"))
    })
    List<Category> selectAllCategoryAndCaseByUserIdAndProjectId(int user_id,int project_id);
}
