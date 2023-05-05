package com.nqff.drms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nqff.drms.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
//    void insertCategory(Category category);
    List<Category> testFunc(int uid, int pid);
}