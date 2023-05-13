package com.nqff.drms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nqff.drms.pojo.Project;
import com.nqff.drms.pojo.User;
import com.nqff.drms.service.ProjectService;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/projects")
@Tag(name = "项目接口")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Operation(summary = "根据用户ID获取所有项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/all/{uid}")
    public Result getAllProjectsByUserId(@PathVariable Integer uid) {
        List<Project> projects = projectService.selectProjectsByUserId(uid);
        if (projects == null || projects.size() == 0) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(projects);
    }

    @Operation(summary = "根据 id 获取指定项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{id}")
    public Result getProjectById(@PathVariable Integer id) {
        Project project = projectService.getById(id);
        if (project == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(project);
    }

    @Operation(summary = "根据关键词模糊查询项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/search/{name}")
    public Result getProjectByName(@PathVariable String name) {
        List<Project> projects = projectService.selectProjectByName(name);
        return Result.SUCCESS(projects);
    }

    @Operation(summary = "根据 id 删除项目", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteProjectById(@PathVariable Integer id) {
        projectService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Project project) {
        projectService.updateById(project);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "新增项目", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "/create")
    public Result createNewProject(@RequestBody Project project) {
        projectService.insertProject(project);
        Map<String, Object> res = new HashMap<>();
        res.put("project_id", project.getId());
        return Result.SUCCESS(res);
    }
}
