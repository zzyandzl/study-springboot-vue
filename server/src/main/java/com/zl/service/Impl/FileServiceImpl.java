package com.zl.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.mapper.FileMapper;
import com.zl.pojo.SysFile;
import com.zl.service.FileService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, SysFile> implements FileService {

    @Mapper
    private FileMapper fileMapper;


    @Override
    public boolean updateFiles(List<SysFile> sysFileList) {

        return false;
    }
}
