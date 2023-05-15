package com.nqff.drms.controller;

import com.nqff.drms.pojo.Plan;
import com.nqff.drms.pojo.Subproject;
import com.nqff.drms.service.PlanService;
import com.nqff.drms.service.SubprojectService;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/search")
@Tag(name = "搜索接口")
public class SearchController {
    @Autowired
    private SubprojectService subprojectService;
    @Autowired
    private PlanService planService;

    @Operation(summary = "根据名字和项目ID获取子项目和方案", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{pid}&{name}")
    public Result getDocumentById(@PathVariable String name,@PathVariable int pid) {
        List<Subproject> subprojects = subprojectService.selectSubProjectByNameAndProjectId(pid,name);
        List<Plan> plans = planService.selectPlansByName(name);
        for(Plan plan : plans){
            Subproject subproject = subprojectService.getById(plan.getId());
            if(subproject != null && subproject.getProjectId() == pid){
                subproject.setPlan(plan);
                subprojects.add(subproject);
            }
        }
        if (subprojects == null || subprojects.size() ==0 ) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(subprojects);
    }
}
