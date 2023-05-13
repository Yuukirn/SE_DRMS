package com.nqff.drms.controller;

import com.nqff.drms.pojo.Plan;
import com.nqff.drms.service.PlanService;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/plans")
@Tag(name = "方案接口")
public class PlanController {
    @Autowired
    private PlanService planService;

//    @Autowired
//    private Algorithm algorithm;

//    @Operation(summary = "根据描述获取相似案例", security = {@SecurityRequirement(name = "Authorization")})
//    @GetMapping(path = "/similar/{description}")
//    public Result getSimilarExample(@PathVariable String description) {
//        return algorithm.getSimilarExample(description);
//    }

    @Operation(summary = "新增方案", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "")
    public Result createNewProject(@RequestBody Plan plan) {
        if(planService.isPlanExist(plan.getSubprojectId())){
            return Result.FAIL("this subproject has plan");
        }
        planService.insertPlan(plan);
        Map<String, Object> res = new HashMap<>();
        res.put("plan_id", plan.getId());
        return Result.SUCCESS(res);
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

    @Operation(summary = "更新方案信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Plan plan) {
        planService.updateById(plan);
        return Result.SUCCESS(null);
    }
}
