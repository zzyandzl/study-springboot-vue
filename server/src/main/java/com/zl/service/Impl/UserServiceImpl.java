package com.zl.service.Impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.dto.UserDto;
import com.zl.mapper.UserMapper;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private static final Log LOG = Log.get();
    @Autowired
    private UserMapper userMapper;

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
    public boolean login(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        // 处理异常情况,防止查询出来的数据是多条数据报错
        try {
            User user = getOne(queryWrapper);
            if(user != null){
                return true;
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

}
