package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {
    private Integer id;
    private String name;
    private Byte type;
    private String description;
    @TableField("user_id")
    private Integer userId;
    @TableLogic
    private Integer deleted;
}
