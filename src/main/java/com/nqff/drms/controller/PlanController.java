package com.nqff.drms.controller;

import com.nqff.drms.algorithm.Algorithm;
import com.nqff.drms.pojo.Document;
import com.nqff.drms.pojo.Plan;
import com.nqff.drms.service.PlanService;
import com.nqff.drms.utils.FileUtils;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/plans")
@Tag(name = "方案接口")
public class PlanController {
    @Autowired
    private PlanService planService;
    @Autowired
    private Algorithm algorithm;

    @Operation(summary = "新增方案", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "/create/{subproject_id}&{user_id}")
    public Result createNewProject(@PathVariable Integer subproject_id,@PathVariable Integer user_id,@RequestBody List<Plan> plans) {
        if(planService.isPlanExist(subproject_id)){
            return Result.FAIL("this subproject has plan");
        }
        Plan plan = algorithm.generateNewPlan(plans,subproject_id,user_id);
        planService.insertPlan(plan);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "根据 id 删除方案", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deletePlanById(@PathVariable Integer id) {
        planService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "根据 id 获取指定方案信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{id}")
    public Result getPlanById(@PathVariable Integer id) {
        Plan plan = planService.selectPlanById(id);
        if (plan == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(plan);
    }
    @Operation(summary = "根据 id 导出指定方案", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/export/{id}")
    public Result downloadDocumentById(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        Plan plan = planService.getById(id);
        if (plan == null) {
            return Result.FAIL("not found");
        }
        String description = plan.getDescription();
        if(description == null){
            description = "";
        }
        String file ="方案描述：\n" + description;
        ServletOutputStream os = response.getOutputStream();
        byte[] b = file.getBytes();
        os.write(b, 0, b.length);
        return null;
    }

    @Operation(summary = "更新方案信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Plan plan) {
        planService.updateById(plan);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "根据子项目id获取相似方案", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/similar/{id}")
    public Result getSimilarPlans(@PathVariable Integer id) {
        List<Plan> plans = algorithm.getSimilarPlans(id);
        return Result.SUCCESS(plans);
    }


}
