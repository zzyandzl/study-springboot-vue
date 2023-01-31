package com.zl.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.SysFileMapper;
import com.zl.pojo.SysFile;
import com.zl.service.SysFileService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    @Mapper
    private SysFileMapper sysFileMapper;


    @Override
    public boolean updateFiles(List<SysFile> sysFileList) {

        return false;
    }
}
