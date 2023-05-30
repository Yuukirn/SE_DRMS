package com.nqff.drms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SubprojectDao extends BaseMapper<Subproject> {

}
