package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Document {
    private Integer id;
    private String uuid;
    private String name;
    private Byte type;
    @TableField("file_path")
    private String filePath;
    @TableField("subproject_id")
    private Integer subprojectId;
    @TableField("user_id")
    private Integer userId;
    @TableLogic
    private Integer deleted;
}
