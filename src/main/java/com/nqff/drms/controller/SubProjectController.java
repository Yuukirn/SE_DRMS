package com.nqff.drms.controller;

        import com.nqff.drms.pojo.Subproject;
        import com.nqff.drms.service.SubprojectService;
        import com.nqff.drms.utils.Result;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.security.SecurityRequirement;
        import io.swagger.v3.oas.annotations.tags.Tag;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping(path = "/sub_projects")
@Tag(name = "子项目接口")
public class SubProjectController {
    @Autowired
    private SubprojectService subProjectService;

    @Operation(summary = "根据项目id获取指定子项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/all/{pid}")
    public Result getAllSubProjectByProjectId(@PathVariable Integer pid) {
        List<Subproject> subprojects = subProjectService.selectAllSubProjectByProjectId(pid);
        if (subprojects == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(subprojects);
    }
    @Operation(summary = "根据子项目id获取指定子项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{id}")
    public Result getDocumentById(@PathVariable Integer id) {
        Subproject subproject = subProjectService.selectById(id);
        if (subproject == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(subproject);
    }
    @Operation(summary = "根据 id 删除子项目", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteProjectById(@PathVariable Integer id) {
        subProjectService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新子项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Subproject SubProject) {
        subProjectService.updateSubprojectById(SubProject);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "新增子项目", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "")
    public Result createNewProject(@RequestBody Subproject SubProject) {
        subProjectService.insertSubProject(SubProject);
        return Result.SUCCESS(null);
    }

}
