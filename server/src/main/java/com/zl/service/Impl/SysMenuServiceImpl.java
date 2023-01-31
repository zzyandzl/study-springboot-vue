package com.zl.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.SysMenuMapper;
import com.zl.pojo.SysMenu;
import com.zl.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> queryAllMenu(String name) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
//      查询所有数据
        List<SysMenu> sysMenuList = list(queryWrapper);
//      查询一级菜单
        List<SysMenu> parentNode = sysMenuList.stream().filter(sysMenu -> sysMenu.getPid() == null).collect(Collectors.toList());
//        System.out.println("sysMenuList01======>"+sysMenuList);
//        System.out.println("parentNode01====>"+parentNode);
        //      查询一级菜单的子菜单
        for (SysMenu menu : parentNode){
//          筛选出二级菜单pid=一级菜单id的就是二级菜单
            menu.setChildren(sysMenuList.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
//        System.out.println("sysMenuList02======>"+sysMenuList);
//        System.out.println("parentNode02====>"+parentNode);
        return parentNode;
    }

}
