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
    @TableField("sub_project_id")
    private Integer subProjectId;
    @TableField("user_id")
    private Integer userId;
    @TableField(exist = false)
    private List<Document> documents;
    @TableField(exist = false)
    private List<Keyword> keywords;
    @TableLogic
    private Integer deleted;
}
