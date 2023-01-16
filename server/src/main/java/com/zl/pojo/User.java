package com.zl.pojo;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@TableName("sys_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @Alias("用户名称")
    private String username;

    @JsonIgnore//加载时不展示该字段
    private String password;

    @Alias("昵称")
    private String nickname;

    @Alias("邮箱")
    private String email;

    @Alias("电话号码")
    private String phone;

    @Alias("地址")
    private String address;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;

    @TableField(value = "avatar_url")  // 指定数据库的字段名称
    private String avatarUrl;
}
