package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.SubprojectKeywordRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubprojectKeywordRelationDao extends BaseMapper<SubprojectKeywordRelation> {
    @Select("select * from `subproject_keyword_relation` where subproject_id = #{subprojectId} and deleted = 0")
    List<SubprojectKeywordRelation> selectBySubprojectId(int subprojectId);
    @Select("select * from `subproject_keyword_relation` where keyword_id = #{keyword_id} and deleted = 0")
    List<SubprojectKeywordRelation> selectByKeywordId(int keyword_id);
    @Select("select * from `subproject_keyword_relation` where keyword_id = #{keyword_id} and subproject_id = #{subprojectId} and deleted = 0")
    List<SubprojectKeywordRelation> selectBySubprojectIdAndKeywordId(int subprojectId,int keyword_id);
}
