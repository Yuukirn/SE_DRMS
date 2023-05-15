package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Document;

import java.util.List;

public interface DocumentService extends IService<Document> {
    void insertDocument(Document document);
    List<Document> selectDocumentsBySubprojectId(int subprojectId);
}
