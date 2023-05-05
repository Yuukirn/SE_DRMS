package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Case;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CaseDao extends BaseMapper<Case> {
    @Select("select * from `case` where category_id = #{category_id} and project_id = #{project_id} and user_id = #{user_id}")
    Case selectByUserIdAndProjectIdAndCategoryId(int user_id, int project_id,int category_id);
}
