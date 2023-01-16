package com.zl.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.UserMapper;
import com.zl.pojo.User;
import com.zl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

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

}
