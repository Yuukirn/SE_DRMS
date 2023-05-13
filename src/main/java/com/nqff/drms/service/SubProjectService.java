package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.SubProject;

import java.util.List;

public interface SubProjectService extends IService<SubProject> {
    void insertSubProject(SubProject SubProject);
    List<SubProject> selectAllSubProjectByProjectId(int pid);

    List<SubProject> selectSubProjectByNameAndPid(int pid, String name);
}