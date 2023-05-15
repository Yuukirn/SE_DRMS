package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Document;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DocumentDao extends BaseMapper<Document> {
}

