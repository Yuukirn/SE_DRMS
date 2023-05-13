package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.KeywordDao;
import com.nqff.drms.dao.SubprojectDao;
import com.nqff.drms.dao.SubprojectKeywordRelationDao;
import com.nqff.drms.pojo.Keyword;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.pojo.SubprojectKeywordRelation;
import com.nqff.drms.service.SubprojectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubprojectServiceImpl extends ServiceImpl<SubprojectDao, Subproject> implements SubprojectService {
    @Autowired
    private SubprojectDao subProjectDao;
    @Autowired
    private SubprojectKeywordRelationDao subProjectKeywordRelationDao;
    @Autowired
    private KeywordDao keywordDao;
    @Override
    public void insertSubProject(Subproject SubProject) {
        subProjectDao.insert(SubProject);
        int plan_id = SubProject.getId();
        for(Keyword keyword : SubProject.getKeywords()){
            SubprojectKeywordRelation relation = new SubprojectKeywordRelation();
            relation.setSubProjectId(plan_id);
            relation.setKeywordId(keyword.getId());
            subProjectKeywordRelationDao.insert(relation);
        }
    }

    @Override
    public List<Subproject> selectAllSubProjectByProjectId(int pid){
        return subProjectDao.selectAllSubProjectByProjectId(pid);
    }

    @Override
    public List<Subproject> selectSubProjectByNameAndPid(int pid, String name) {
        return subProjectDao.selectSubProjectByNameAndPid(pid,'%'+name+'%');
    }

    @Override
    public List<Subproject> selectSubprojectByKeywordId(int keyword_id) {
        List<SubprojectKeywordRelation> relations = subProjectKeywordRelationDao.selectByKeywordId(keyword_id);
        List<Subproject> subprojects = new ArrayList<Subproject>();
        for(SubprojectKeywordRelation relation : relations){
            subprojects.add(subProjectDao.selectById(relation.getSubProjectId()));
        }
        return subprojects;
    }

    @Override
    public Subproject selectById(int id) {
        Subproject subproject = subProjectDao.selectById(id);
        List<SubprojectKeywordRelation> relations = subProjectKeywordRelationDao.selectBySubprojectId(subproject.getId());
        List<Keyword> keywords = new ArrayList<Keyword>();
        for(SubprojectKeywordRelation relation : relations){
            keywords.add(keywordDao.selectById(relation.getKeywordId()));
        }
        subproject.setKeywords(keywords);
        return subproject;
    }

    @Override
    public void updateSubprojectById(Subproject subproject) {
        Subproject pre = selectById(subproject.getId());
        if(pre == null)
            return;

        List<Keyword> preKeywords = pre.getKeywords();
        List<Keyword> curKeywords = subproject.getKeywords();
        int subprojectId = subproject.getId();
        for(Keyword keyword : preKeywords){
            System.out.println(keyword);
            if(!curKeywords.contains(keyword)){
                subProjectKeywordRelationDao.deleteById(subProjectKeywordRelationDao.selectBySubprojectIdAndKeywordId(subprojectId,keyword.getId()));
            }
            else {
                curKeywords.remove(keyword);
            }
        }

        for(Keyword keyword : curKeywords){
            SubprojectKeywordRelation relation = new SubprojectKeywordRelation();
            relation.setSubProjectId(subprojectId);
            relation.setKeywordId(keyword.getId());
            subProjectKeywordRelationDao.insert(relation);
        }
        updateById(subproject);
    }
}
