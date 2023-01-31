package com.zl.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.SysRoleMapper;
import com.zl.mapper.SysRoleMenuMapper;
import com.zl.pojo.SysMenu;
import com.zl.pojo.SysRole;
import com.zl.pojo.SysRoleMenu;
import com.zl.service.SysMenuService;
import com.zl.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 先删除当前角色id所有的绑定关系
        sysRoleMenuMapper.deleteByRoleId(roleId);

        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
//            SysMenu menu = sysMenuService.getById(menuId);
//            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
//                // 二级菜单 并且传过来的menuId数组里面没有它的父级id
//                // 那么我们就得补上这个父级id
//                RoleMenu roleMenu = new RoleMenu();
//                roleMenu.setRoleId(roleId);
//                roleMenu.setMenuId(menu.getPid());
//                roleMenuMapper.insert(roleMenu);
//                menuIdsCopy.add(menu.getPid());
//            }
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {

        return sysRoleMenuMapper.selectByRoleId(roleId);
    }
}
