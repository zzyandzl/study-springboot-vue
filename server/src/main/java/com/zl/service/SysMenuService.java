package com.zl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.pojo.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> queryAllMenu(String name);
}
