package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Category {
    private Integer id;
    private String name;
    @TableField("project_id")
    private Integer projectId;
    @TableField("user_id")
    private Integer userId;
    @TableField(exist = false)
    private List<Example> examples;
    @TableLogic
    private Integer deleted;
}
