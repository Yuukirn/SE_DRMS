package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.SubProjectDao;
import com.nqff.drms.pojo.SubProject;
import com.nqff.drms.service.SubProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectServiceImpl extends ServiceImpl<SubProjectDao, SubProject> implements SubProjectService {
    @Autowired
    private SubProjectDao subProjectDao;
    @Override
    public void insertSubProject(SubProject SubProject) {
        subProjectDao.insert(SubProject);
    }

    @Override
    public List<SubProject> selectAllSubProjectByProjectId(int pid){
        return subProjectDao.selectAllSubProjectByProjectId(pid);
    }

    @Override
    public List<SubProject> selectSubProjectByNameAndPid(int pid, String name) {
        return subProjectDao.selectSubProjectByNameAndPid(pid,'%'+name+'%');
    }
}
