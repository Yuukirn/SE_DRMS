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
}
