package com.nqff.drms.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Keyword implements Comparable<Keyword> {
    private Integer id;
    private String name;
    @TableLogic
    private Integer deleted;
    @TableField(exist = false)
    private double weight;

    @Override
    public int compareTo(Keyword o) {
        if (this.weight > o.weight) {
            return -1;
        } else if (this.weight == o.weight) {
            return 0;
        } else {
            return 1;
        }
    }

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
            return this.name.equals(kw.getName());
        }
        return false;
    }

}
