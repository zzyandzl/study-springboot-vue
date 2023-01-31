package com.zl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu菜单对象", description = "")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("描述")
    private String description;

    /*自查询的父级*/
    @ApiModelProperty("父级id")
    private Integer pid;

    /*展示给前台的菜单数据，在数据库不存在该字段*/
    @TableField(exist = false)
    private List<SysMenu> children;

    @ApiModelProperty("页面路径")
    @TableField(value = "page_path")
    private String pagePath;

    @ApiModelProperty("排序")
    @TableField(value = "sort_num")
    private String sortNum;
}
