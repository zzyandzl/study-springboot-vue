package com.zl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.pojo.User;

import java.util.List;

public interface UserService extends IService<User> {
    boolean saveUser(User user);

    boolean deleteIds(List<Integer> ids);
}
