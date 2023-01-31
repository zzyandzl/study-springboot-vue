package com.zl.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zl.common.Result;
import com.zl.mapper.SysMenuMapper;
import com.zl.pojo.SysMenu;
import com.zl.service.SysMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", response = Result.class)
    public Result<?> findAllByPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name){
        IPage<SysMenu> page = new Page(pageNum,pageSize);
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        queryWrapper.orderByDesc("id");
        IPage<SysMenu> userIPage = sysMenuService.page(page, queryWrapper);
        return Result.susscess(userIPage);
    }

    @GetMapping("/all")
    @ApiOperation(value = "全部菜单", response = Result.class)
    public Result<?> queryAllMenu(@RequestParam(defaultValue = "") String name){
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
//      查询所有数据
        List<SysMenu> sysMenuList = sysMenuService.list(queryWrapper);
//      查询一级菜单
        List<SysMenu> parentNode = sysMenuList.stream().filter(sysMenu -> sysMenu.getPid() == null).collect(Collectors.toList());
//      查询一级菜单的子菜单
        for (SysMenu menu : parentNode){
//          筛选出二级菜单pid=一级菜单id的就是二级菜单
            menu.setChildren(sysMenuList.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return Result.susscess(parentNode);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增菜单信息", response = Result.class)
    public Result<?> addMenu(@RequestBody SysMenu sysMenu){
        boolean flag = sysMenuService.save(sysMenu);
        if(flag){
            return Result.susscess("新增成功");
        }else{
            return Result.error("-1","新增失败");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新菜单信息", response = Result.class)
    public Result<?> updateMenu(@RequestBody SysMenu sysMenu){
        boolean flag = sysMenuService.updateById(sysMenu);
        if(flag){
            return Result.susscess("更新成功");
        }else{
            return Result.error("-1","更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除菜单信息", response = Result.class)
    public Result<?> delete(@PathVariable("id") Integer id) {
        boolean flag = sysMenuService.removeById(id);
        if(flag){
            return Result.susscess("删除成功");
        }else{
            return Result.error("-1","删除失败");
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除菜单信息", response = Result.class)
    public Result<?> BatchDeleteMenu(@RequestBody List<Integer> ids){
        System.out.println("ids=====>"+ids);
        boolean flag = sysMenuService.removeBatchByIds(ids);
        if(flag){
            return Result.susscess("批量删除成功");
        }else{
            return Result.error("-1","批量删除失败");
        }
    }
}
