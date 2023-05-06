package com.nqff.drms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nqff.drms.dao.CategoryDao;
import com.nqff.drms.pojo.Category;
import com.nqff.drms.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    @Override
    public void insertCategory(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public List<Category> selectAllCategoryByProjectId(int pid){
        return categoryDao.selectAllCategoryByProjectId(pid);
    }
}
