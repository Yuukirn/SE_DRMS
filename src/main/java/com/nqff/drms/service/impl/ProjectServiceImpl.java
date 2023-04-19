package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.ProjectDao;
import com.nqff.drms.pojo.Project;
import com.nqff.drms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, Project> implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public void insertProject(Project project) {
        projectDao.insert(project);
    }
}
