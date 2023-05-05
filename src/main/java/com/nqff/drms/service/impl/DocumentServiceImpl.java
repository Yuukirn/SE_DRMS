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

//    @Override
//    public Document getDocumentByParentId(Integer parent_id) {
//        LambdaQueryWrapper<Document> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(parent_id != null, Document::getParentId, parent_id);
//        List<Document> documents = documentDao.selectList(wrapper);
//        if (documents == null || documents.size() == 0) {
//            return null;
//        }
//        return documents.get(0);
//    }
}
