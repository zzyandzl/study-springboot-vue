package com.zl.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.SysDictMapper;
import com.zl.pojo.SysDict;
import com.zl.service.SysDictService;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
}
