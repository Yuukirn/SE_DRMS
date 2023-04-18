package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Project;

public interface ProjectService extends IService<Project> {
    void insertProject(Project project);
}
