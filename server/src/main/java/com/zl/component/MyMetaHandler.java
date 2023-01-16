package com.zl.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaHandler implements MetaObjectHandler {

    @Override//插入时的填充策略
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        System.out.println("新增时间===》"+new Date());
        this.setFieldValByName("createTime",new Date(),metaObject);
        //第一个参数：字段名；第二个参数：类型；第三个参数：数据内容
    }

    @Override//更新时的填充策略
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
