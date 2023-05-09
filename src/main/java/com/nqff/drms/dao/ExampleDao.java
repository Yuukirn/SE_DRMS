package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Example;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExampleDao extends BaseMapper<Example> {
    @Select("select * from example where category_id = #{category_id} and deleted = 0")
    Example selectByCategoryId(int category_id);

    @Select("select * from `example` where id = #{id} and deleted = 0")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "id",property = "documents",javaType = List.class,
                    many = @Many(select = "com.nqff.drms.dao.DocumentDao.selectByExampleId"))
    })
    Example selectExampleById(int id);
}
