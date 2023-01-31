package com.zl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.pojo.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
