package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Document;

public interface DocumentService extends IService<Document> {
    void insertDocument(Document document);

    Document getDocumentByParentId(Integer parent_id);
}
