package com.zl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.pojo.SysFile;

import java.util.List;

public interface FileService extends IService<SysFile> {
    boolean updateFiles(List<SysFile> sysFileList);
}
