package com.nqff.drms.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nqff.drms.pojo.sub_project;
import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.SubProjctService;
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
    private SubProjctService subProjctService;
    @Autowired
    private ExampleService exampleService;

    @Operation(summary = "根据关键词和项目id查找项目中包含关键词的类别和案例", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{pid}&{name}")
    public Result getProjectByName(@PathVariable int pid,@PathVariable String name) {
        List<sub_project> categories = subProjctService.selectSubProjectByNameAndPid(pid,name);
        List<Example> tmp = exampleService.selectExampleByName(name);
        BaseMapper<sub_project> baseMapper = subProjctService.getBaseMapper();

        HashMap<Integer, sub_project> map = new HashMap<Integer, sub_project>();
        for (sub_project sub_project :categories) {
            map.put(sub_project.getId(), sub_project);
        }

        for (Example example: tmp) {
            sub_project sub_project = baseMapper.selectById(example.getCategoryId());
            if(sub_project == null)
                continue;
            if(sub_project.getProjectId() == pid){
                int id = sub_project.getId();

                if(map.containsKey(id)){
                    sub_project = map.get(id);
                }else {
                    map.put(id, sub_project);
                    categories.add(sub_project);
                }

                List<Example> examples = sub_project.getExamples();
                if(examples == null){
                    examples = new ArrayList<Example>();
                    sub_project.setExamples(examples);
                }
                examples.add(example);
            }
        }
        return Result.SUCCESS(categories);
    }
}
