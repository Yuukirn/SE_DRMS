package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Keyword {
    private Integer id;
    private String name;
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

        if (obj instanceof Keyword) {
            Keyword kw = (Keyword) obj;
            return this.id.equals(kw.getId());
        }
        return false;
    }

}
