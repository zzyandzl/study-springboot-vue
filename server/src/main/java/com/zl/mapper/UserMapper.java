package com.zl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> queryTopage(Integer pageNum, Integer pageSize, String username, String nickname, String address);

    Integer selectTotalPage();

    Integer deleteIds(@Param("ids") List<Integer> ids);
}
