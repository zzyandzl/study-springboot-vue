package com.zl.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zl.common.Constants;
import com.zl.common.Result;
import com.zl.mapper.SysDictMapper;
import com.zl.mapper.SysMenuMapper;
import com.zl.pojo.SysDict;
import com.zl.pojo.SysMenu;
import com.zl.service.SysDictService;
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

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictMapper sysDictMapper;

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
        List<SysMenu> sysMenus = sysMenuService.queryAllMenu(name);
        return Result.susscess(sysMenus);
    }

    @GetMapping("/ids")
    @ApiOperation(value = "全部菜单id", response = Result.class)
    public Result<?> queryAllIds(){
        return Result.susscess(sysMenuService.list().stream().map(SysMenu::getId));
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

    @GetMapping("/icons")
    @ApiOperation(value = "查询菜单图标", response = Result.class)
    public Result<?> queryAllIcons(){
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_ICON.getCode());
        return Result.susscess(sysDictMapper.selectList(queryWrapper));
    }
}
