package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Plan {
    private Integer id;
    private String name;
    private String description;
    @TableField("subproject_id")
    private Integer subprojectId;
    @TableField("user_id")
    private Integer userId;
    @TableField(exist = false)
    private List<Document> documents;
    @TableLogic
    private Integer deleted;
}
