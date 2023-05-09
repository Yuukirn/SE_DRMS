package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Example {
    private Integer id;
    private String name;
    private String description;
    @TableField("category_id")
    private Integer categoryId;
    @TableField("user_id")
    private Integer userId;
    @TableField(exist = false)
    private List<Document> documents;
    @TableField(exist = false)
    private Long simHash;
    @TableLogic
    private Integer deleted;
}
