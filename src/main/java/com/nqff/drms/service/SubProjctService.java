package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.sub_project;

import java.util.List;

public interface SubProjctService extends IService<sub_project> {
    void insertSubProject(sub_project sub_project);
    List<sub_project> selectAllSubProjectByProjectId(int pid);

    List<sub_project> selectSubProjectByNameAndPid(int pid, String name);
}