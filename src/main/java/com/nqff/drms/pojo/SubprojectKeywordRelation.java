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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (obj instanceof SubprojectKeywordRelation) {
            SubprojectKeywordRelation relation = (SubprojectKeywordRelation) obj;
            return this.keywordId.equals(relation.getKeywordId()) & this.subProjectId.equals(relation.getSubProjectId());
        }
        return false;
    }
}
