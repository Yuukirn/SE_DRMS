package com.nqff.drms.controller;

import com.nqff.drms.pojo.Example;
import com.nqff.drms.service.ExampleService;
import com.nqff.drms.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/examples")
@Tag(name = "案例接口")
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

    @Operation(summary = "新增案例", security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping(path = "/create")
    public Result createNewProject(@RequestBody Example example) {
        exampleService.insertExample(example);
        Map<String, Object> res = new HashMap<>();
        res.put("example_id", example.getId());
        return Result.SUCCESS(res);
    }

    @Operation(summary = "根据 id 删除案例", security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping(path = "/{id}")
    public Result deleteExampleById(@PathVariable Integer id) {
        exampleService.removeById(id);
        return Result.SUCCESS(null);
    }

    @Operation(summary = "根据 id 获取指定案例信息", security = {@SecurityRequirement(name = "Authorization")})
    @GetMapping(path = "/{id}")
    public Result getExampleById(@PathVariable Integer id) {
        Example example = exampleService.selectExampleById(id);
        if (example == null) {
            return Result.FAIL("not found", null);
        }
        return Result.SUCCESS(example);
    }

    @Operation(summary = "更新案例信息", security = {@SecurityRequirement(name = "Authorization")})
    @PutMapping()
    public Result updateProjectInfo(@RequestBody Example example) {
        exampleService.updateById(example);
        return Result.SUCCESS(null);
    }
}
