package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.ProjectDao;
import com.nqff.drms.pojo.Project;
import com.nqff.drms.service.ProjectService;
import com.nqff.drms.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, Project> implements ProjectService {
    @Autowired
    private ProjectDao projectDao;

    @Override
    public void insertProject(Project project) {
        projectDao.insert(project);
    }

    @Override
    public List<Project> selectProjectByName(String name) {
        LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<Project>();
        lqw.like(Project::getName, name);
        return projectDao.selectList(lqw);
    }
}
