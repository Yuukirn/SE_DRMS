package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Subproject {
    private Integer id;
    private String name;
    private String description;
    @TableField("project_id")
    private Integer projectId;
    @TableField("user_id")
    private Integer userId;
    @TableField(exist = false)
    private Plan plan;
    @TableField(exist = false)
    private List<Keyword> keywords;
    @TableField(exist = false)
    private List<Document> documents;
    @TableLogic
    private Integer deleted;
}
