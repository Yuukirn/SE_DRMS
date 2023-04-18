package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Document;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocumentDao extends BaseMapper<Document> {
}
