package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Project;

import java.util.List;

public interface ProjectService extends IService<Project> {
    void insertProject(Project project);
    List<Project> selectProjectByName(String name);
    List<Project> selectProjectsByUserId(int user_id);
}
