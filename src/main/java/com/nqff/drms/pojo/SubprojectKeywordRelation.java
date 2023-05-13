package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubprojectKeywordRelation {
    private Integer id;
    @TableField("subproject_id")
    private Integer subProjectId;
    @TableField("keyword_id")
    private Integer keywordId;
    @TableLogic
    private Integer deleted;
}
