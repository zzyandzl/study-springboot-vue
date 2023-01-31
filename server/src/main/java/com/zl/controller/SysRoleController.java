package com.zl.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zl.common.Result;
import com.zl.mapper.SysRoleMapper;
import com.zl.pojo.SysRole;
import com.zl.pojo.User;
import com.zl.service.SysRoleService;
import com.zl.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", response = Result.class)
    public Result<?> findAllByPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name){
        IPage<SysRole> page = new Page(pageNum,pageSize);
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        queryWrapper.orderByDesc("id");
        IPage<SysRole> userIPage = sysRoleService.page(page, queryWrapper);
        return Result.susscess(userIPage);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询所有", response = Result.class)
    public Result<?> findAll(){
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<SysRole> sysRoleList = sysRoleMapper.selectList(queryWrapper);
        return Result.susscess(sysRoleList);
    }

    @GetMapping("/name/{name}")
    @ApiOperation(value = "个人角色查询", response = Result.class)
    public Result<?> queryRole(@PathVariable("name") String name){
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        SysRole sysRole = sysRoleService.getOne(queryWrapper);
        return Result.susscess(sysRole);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增角色信息", response = Result.class)
    public Result<?> addRole(@RequestBody SysRole sysRole){
        boolean flag = sysRoleService.save(sysRole);
        if(flag){
            return Result.susscess("新增成功");
        }else{
            return Result.error("-1","新增失败");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新角色信息", response = Result.class)
    public Result<?> updateRole(@RequestBody SysRole sysRole){
        boolean flag = sysRoleService.updateById(sysRole);
        if(flag){
            return Result.susscess("更新成功");
        }else{
            return Result.error("-1","更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除角色信息", response = Result.class)
    public Result<?> delete(@PathVariable("id") Integer id) {
        boolean flag = sysRoleService.removeById(id);
        if(flag){
            return Result.susscess("删除成功");
        }else{
            return Result.error("-1","删除失败");
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除角色信息", response = Result.class)
    public Result<?> BatchDeleteRole(@RequestBody List<Integer> ids){
        System.out.println("ids=====>"+ids);
        boolean flag = sysRoleService.removeBatchByIds(ids);
        if(flag){
            return Result.susscess("批量删除成功");
        }else{
            return Result.error("-1","批量删除失败");
        }
    }

    @PostMapping("/rolemenu/{roleId}")
    @ApiOperation(value = "角色菜单绑定", response = Result.class)
    public Result<?> RoleMenu(@PathVariable("roleId") Integer roleId,@RequestBody List<Integer> menuIds){
        sysRoleService.setRoleMenu(roleId,menuIds);
        return Result.susscess();
    }

    @GetMapping("/rolemenu/{roleId}")
    @ApiOperation(value = "个人角色菜单", response = Result.class)
    public Result<?> RoleMenu(@PathVariable("roleId") Integer roleId){
        List<Integer> roleMenu = sysRoleService.getRoleMenu(roleId);
        return Result.susscess(roleMenu);
    }
}
