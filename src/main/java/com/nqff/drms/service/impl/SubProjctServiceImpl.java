package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.SubProjectDao;
import com.nqff.drms.pojo.sub_project;
import com.nqff.drms.service.SubProjctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjctServiceImpl extends ServiceImpl<SubProjectDao, sub_project> implements SubProjctService {
    @Autowired
    private SubProjectDao subProjectDao;
    @Override
    public void insertSubProject(sub_project sub_project) {
        subProjectDao.insert(sub_project);
    }

    @Override
    public List<sub_project> selectAllSubProjectByProjectId(int pid){
        return subProjectDao.selectAllSubProjectByProjectId(pid);
    }

    @Override
    public List<sub_project> selectSubProjectByNameAndPid(int pid, String name) {
        return subProjectDao.selectSubProjectByNameAndPid(pid,'%'+name+'%');
    }
}
