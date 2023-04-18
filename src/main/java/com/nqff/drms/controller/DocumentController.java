package com.nqff.drms.controller;

import com.alibaba.fastjson.JSONObject;
import com.nqff.drms.pojo.Document;
import com.nqff.drms.pojo.Project;
import com.nqff.drms.service.DocumentService;
import com.nqff.drms.utils.FileUtils;
import com.nqff.drms.utils.Result;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(path = "/documents")
@Tag(name = "资料接口")
public class DocumentController {
    private static final Byte DOC_TYPE_WORD = 1;
    private static final Byte DOC_TYPE_PDF = 2;
    private static final String DOC_PATH = FileUtils.FILE_PATH + File.separator + "docs";

    @Autowired
    private DocumentService documentService;

//    @Operation(summary = "获取所有资料信息", security = {@SecurityRequirement(name = "Authorization")})
//    @GetMapping
//    public Result getAllDocuments() {
//        List<Document> documents = documentService.list();
//        if (documents == null || documents.size() == 1) {
//            return Result.FAIL("not found", null);
//        }
//        return Result.SUCCESS(documents);
//    }

    @Operation(summary = "根据 id 获取指定资料信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{id}")
    public Result getDocumentById(@PathVariable Integer id) {
        Document document = documentService.getById(id);
        if (document == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(document);
    }

    @Operation(summary = "获取指定文件夹下的资料信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping
    public Result getDocumentByParentId(@RequestParam(name = "parent_id", required = false) Integer parent_id) {
        if (parent_id == null) {
            List<Document> documents = documentService.list();
            if (documents == null || documents.size() == 0) {
                return Result.FAIL("not found", null);
            }
            return Result.SUCCESS(documents);
        } else {
            Document document = documentService.getDocumentByParentId(parent_id);
            if (document == null) {
                return Result.FAIL("not found", null);
            }
            return Result.SUCCESS(document);
        }
    }

    @Operation(summary = "根据 id 删除资料", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteDocumentById(@PathVariable Integer id) {
        documentService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新资料信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping
    public Result updateDocumentInfo(@RequestBody Document document) {
        documentService.updateById(document);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "上传资料", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping("/upload")
    public Result uploadDocument(@RequestParam(value = "file") MultipartFile file,
                         @RequestParam(value = "parent_id") Integer parent_id,
                         @RequestParam(value = "project_id") Integer project_id,
                         @RequestParam(value = "user_id") Integer user_id) {
        try {
            String path = "/Users/yuuki/DRMS";
            File uploadDir = new File(path);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String filename = file.getOriginalFilename();
            File localFile = new File(uploadDir + File.separator + filename);
            if (localFile.exists()) {
                return Result.FAIL("file exists");
            }

            int index = filename.lastIndexOf('.');
            String ext = filename.substring(index + 1);
            Byte type = null;
            if (ext.equals("doc")) {
                type = DOC_TYPE_WORD;
            } else if (ext.equals("pdf")) {
                type = DOC_TYPE_PDF;
            }
            file.transferTo(localFile);
            Document document = new Document();
            document.setName(filename);
            document.setFilePath(localFile.getPath());
            document.setType(type);
            document.setParentId(parent_id);
            document.setProjectId(project_id);
            document.setUserId(user_id);
            documentService.insertDocument(document);
        } catch (Exception e) {
            return Result.FAIL(e.getMessage(), null);
        }
        return Result.SUCCESS(null);
    }

    @Operation(summary = "创建文件夹", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping("/create-folder")
    public Result createFolder() {
        return null;
    }
}
