package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Subproject;

import java.util.List;

public interface SubprojectService extends IService<Subproject> {
    void insertSubProject(Subproject SubProject);
    List<Subproject> selectAllSubProjectByProjectId(int pid);
    List<Subproject> selectSubProjectByNameAndProjectId(int pid, String name);
    List<Subproject> selectSubprojectByKeywordId(int keyword_id);
    Subproject selectById(int id);
    void updateSubprojectById(Subproject subproject);
}