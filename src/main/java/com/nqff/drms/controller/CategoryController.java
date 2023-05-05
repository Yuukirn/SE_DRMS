package com.nqff.drms.controller;

        import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
        import com.nqff.drms.pojo.Category;
        import com.nqff.drms.pojo.Document;
        import com.nqff.drms.pojo.Project;
        import com.nqff.drms.pojo.User;
        import com.nqff.drms.service.CategoryService;
        import com.nqff.drms.service.ProjectService;
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

    @Operation(summary = "根据 id 获取指定资料信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{uid}&{pid}")
    public Result getDocumentById(@PathVariable Integer uid ,@PathVariable Integer pid) {
        System.out.println(uid + " " + pid);
        List<Category> categories = categoryService.testFunc(uid,pid);
        System.out.println(categories.size());
        if (categories == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(categories);
    }
}
