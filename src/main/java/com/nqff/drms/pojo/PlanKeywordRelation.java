package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanKeywordRelation {
    @TableField("plan_id")
    private int planId;
    @TableField("keywor_id")
    private int keywordId;
}
