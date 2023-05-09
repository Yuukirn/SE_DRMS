package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DocumentDao extends BaseMapper<Document> {
    @Select("select * from document where example_id = #{example_id} and deleted = 0")
    Document selectByExampleId(int example_id);
}
