package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Example;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

@Mapper
public interface ExampleDao extends BaseMapper<Example> {
    @Select("select * from example where category_id = #{category_id}")
    Example selectByCategoryId(int category_id);
}
