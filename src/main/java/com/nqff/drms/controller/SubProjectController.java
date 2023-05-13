package com.nqff.drms.controller;

        import com.nqff.drms.pojo.sub_project;
        import com.nqff.drms.service.SubProjctService;
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
    private SubProjctService subProjctService;

    @Operation(summary = "根据项目id获取指定子项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{pid}")
    public Result getDocumentById(@PathVariable Integer pid) {
        List<sub_project> sub_projects = subProjctService.selectAllSubProjectByProjectId(pid);
        if (sub_projects == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(sub_projects);
    }
    @Operation(summary = "根据 id 删除子项目", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteProjectById(@PathVariable Integer id) {
        subProjctService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新子项目信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody sub_project sub_project) {
        subProjctService.updateById(sub_project);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "新增子项目", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "/create")
    public Result createNewProject(@RequestBody sub_project sub_project) {
        subProjctService.insertSubProject(sub_project);
        return Result.SUCCESS(null);
    }

}
