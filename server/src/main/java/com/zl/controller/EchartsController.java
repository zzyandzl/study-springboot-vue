package com.zl.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.zl.common.Result;
import com.zl.pojo.User;
import com.zl.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;

    @GetMapping("/example")
    @ApiOperation(value = "Echarts图标案例", response = Result.class)
    public Result<?> get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon","Tue","Wed","Thu","Fri","Sat","Sun"));
        map.put("y",CollUtil.newArrayList(150,230,224,218,135,147,260));
        return Result.susscess(map);
    }

    @GetMapping("/members")
    @ApiOperation(value = "各季度会员数量统计", response = Result.class)
    public Result<?> members() {
        List<User> userList = userService.list();
        int q1 = 0;//第一季度
        int q2 = 0;//第二季度
        int q3 = 0;//第三季度
        int q4 = 0;//第四季度
        for(User user : userList){
            Date createTime = user.getCreateTime();
//          DateUtil.quarterEnum:获得指定日期所属季度
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1+=1; break;
                case Q2: q2+=1; break;
                case Q3: q3+=1; break;
                case Q4: q4+=1; break;
                default: break;
            }
        }
        return Result.susscess(CollUtil.newArrayList(q1,q2,q3,q4));
    }
 }
