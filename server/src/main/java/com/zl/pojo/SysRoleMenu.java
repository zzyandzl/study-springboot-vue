package com.zl.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("sys_role_menu")
@Data
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer roleId;
    private Integer menuId;
}
