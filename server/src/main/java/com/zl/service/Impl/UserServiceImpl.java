package com.zl.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.common.Constants;
import com.zl.common.ServiceException;
import com.zl.dto.UserDto;
import com.zl.mapper.SysMenuMapper;
import com.zl.mapper.SysRoleMapper;
import com.zl.mapper.SysRoleMenuMapper;
import com.zl.mapper.UserMapper;
import com.zl.pojo.SysMenu;
import com.zl.pojo.User;
import com.zl.service.SysMenuService;
import com.zl.service.UserService;
import com.zl.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private static final Log LOG = Log.get();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    @Override
    public boolean deleteIds(List<Integer> ids) {
        boolean flag = false;
        Integer deleteIds = userMapper.deleteIds(ids);
        if(deleteIds > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public UserDto login(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        User user = new User();
        // 处理异常情况,防止查询出来的数据是多条数据报错
        try {
            user = getOne(queryWrapper);
            System.out.println("user======>"+user);
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_402.getCode(), "系统错误");
        }
        if(user != null){
            /*数据复制,将原来数据复制到新变量中，
             * 参数一：原数据
             * 参数二：目标数据，数据复制到这边
             * 参数三：忽略大小写
             * */
            BeanUtils.copyProperties(user,userDto);
//          创建token,并将值传给dto
            String token = TokenUtils.getToken(user.getId().toString(), user.getPassword());
            userDto.setToken(token);

//          获取当前用户的角色权限
            String role = user.getRole(); // ROLE_ADMIN
            // 获取当前用户的菜单列表
            List<SysMenu> roleMenus = getRoleMenus(role);

            userDto.setMenus(roleMenus);

            return userDto;
        }else{
            throw new ServiceException(Constants.CODE_600.getCode(),"用户名密码错误");
        }
    }

    @Override
    public UserDto register(UserDto userDto) {
        User user = getUserInfo(userDto);
        if(user != null){
            throw new ServiceException(Constants.CODE_600.getCode(), "用户已存在");
        }else{
//            System.out.println("user=========>"+user);
            user = new User();
//            System.out.println("user01=========>"+user);
            BeanUtils.copyProperties(userDto,user);
//          将用户信息存入
            save(user);
        }
        return userDto;
    }

    private User getUserInfo(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        User user = new User();
        try {
            user = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_402.getCode(), "系统错误");
        }
        return user;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<SysMenu> getRoleMenus(String roleFlag) {
        Integer roleId = sysRoleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = sysRoleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<SysMenu> menus = sysMenuService.queryAllMenu("");
        // new一个最后筛选完成之后的list
        List<SysMenu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (SysMenu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<SysMenu> children = menu.getChildren();
            // java8新特性：removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
