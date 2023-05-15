package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.DocumentDao;
import com.nqff.drms.pojo.Document;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentDao, Document> implements DocumentService {
    @Autowired
    private DocumentDao documentDao;
    @Override
    public void insertDocument(Document document) {
        documentDao.insert(document);
    }

    @Override
    public List<Document> selectDocumentsBySubprojectId(int subprojectId) {
        LambdaQueryWrapper<Document> wrapper = new LambdaQueryWrapper<Document>();
        wrapper.eq(Document::getSubprojectId,subprojectId);
        return documentDao.selectList(wrapper);
    }

}
