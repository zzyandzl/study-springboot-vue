package com.zl.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@TableName("sys_dict")
@Data
@ApiOperation("图标表")
public class SysDict {

    private String name;
    private String value;
    private String type;
}
