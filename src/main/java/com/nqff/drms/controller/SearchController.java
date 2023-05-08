package com.nqff.drms.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.Category;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.CategoryService;
import com.nqff.drms.service.ExampleService;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/search")
@Tag(name = "搜索接口")
public class SearchController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ExampleService exampleService;

    @Operation(summary = "根据关键词和项目id查找项目中包含关键词的类别和案例", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{pid}&{name}")
    public Result getProjectByName(@PathVariable int pid,@PathVariable String name) {
        List<Category> categories = categoryService.selectCategoryByNameAndPid(pid,name);
        List<Example> tmp = exampleService.selectExampleByName(name);
        BaseMapper<Category> baseMapper = categoryService.getBaseMapper();

        HashMap<Integer,Category> map = new HashMap<Integer,Category>();
        for (Category category:categories) {
            map.put(category.getId(),category);
        }

        for (Example example: tmp) {
            Category category = baseMapper.selectById(example.getCategoryId());
            if(category == null)
                continue;
            if(category.getProjectId() == pid){
                int id = category.getId();

                if(map.containsKey(id)){
                    category = map.get(id);
                }else {
                    map.put(id, category);
                    categories.add(category);
                }

                List<Example> examples = category.getExamples();
                if(examples == null){
                    examples = new ArrayList<Example>();
                    category.setExamples(examples);
                }
                examples.add(example);
            }
        }
        return Result.SUCCESS(categories);
    }
}
