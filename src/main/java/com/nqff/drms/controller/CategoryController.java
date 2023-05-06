package com.nqff.drms.controller;

        import com.nqff.drms.pojo.Category;
        import com.nqff.drms.service.CategoryService;
        import com.nqff.drms.utils.Result;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.security.SecurityRequirement;
        import io.swagger.v3.oas.annotations.tags.Tag;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Tag(name = "类别接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "根据用户id和项目id获取指定类别信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{pid}")
    public Result getDocumentById(@PathVariable Integer pid) {
        List<Category> categories = categoryService.selectAllCategoryByProjectId(pid);
        if (categories == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(categories);
    }
    @Operation(summary = "根据 id 删除类别", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteProjectById(@PathVariable Integer id) {
        categoryService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "更新类别信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "新增类别", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "/create")
    public Result createNewProject(@RequestBody Category category) {
        categoryService.insertCategory(category);
        return Result.SUCCESS(null);
    }

}
