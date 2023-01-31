package com.zl.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zl.common.Result;
import com.zl.mapper.SysFileMapper;
import com.zl.pojo.SysFile;
import com.zl.service.SysFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysFileMapper sysFileMapper;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    /**
     * 文件上传接口
     * @param file 前端传入的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传", response = Result.class)
    public Result<?> upload(@RequestParam MultipartFile file) throws IOException {
//      获取上传的文件名
        String originalFilename = file.getOriginalFilename();
//      获取文件类型
        String type = FileUtil.extName(originalFilename);
//      获取文件大小
        long size = file.getSize();
//      将文件存储到自定义的文件夹下，路径定义在qpplication.yml中,也就是创建文件存储的父级目录fileUploadPath
        File uploadParentFile = new File(fileUploadPath);
//       判断配置的父级目录是否存在，不存在则创建新的文件夹
        if (!uploadParentFile.exists()){
//          用mkdirs()可以创建多级目录
            uploadParentFile.mkdirs();
        }
//      为文件设置唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        /*
        * uuid +StrUtil.DOT + type：自定义的文件名称
        * */
        String fileUUid = uuid +StrUtil.DOT + type;
//      创建文件url路径
        String url;
//      获取文件MD5
        String md5 = SecureUtil.md5(file.getInputStream());
//      根据MD5查询是否有相同的文件被上传
        SysFile fileByMD5 = getFileByMD5(md5);
//      文件如果重复
        if(fileByMD5 != null){
            url = fileByMD5.getUrl();
        }else{//文件不重复
            File uploadFile = new File(fileUploadPath + fileUUid);
//      把获取到的文件存储在自定义的磁盘目录下
            file.transferTo(uploadFile);
//      将数据存储到数据库中
            url = "http://localhost:9090/file/download/" + fileUUid;
        }
        SysFile sysFile = new SysFile();
        sysFile.setName(originalFilename);
        sysFile.setType(type);
//      将大小转成kb
        sysFile.setSize(size / 1024);
        sysFile.setUrl(url);
        sysFile.setMd5(md5);
        sysFileService.save(sysFile);
        return Result.susscess(url);
    }

    /**
     * 文件下载接口   http://localhost:9090/file/download/{fileUUID}
     * @param fileUUid
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/{fileUUid}")
    @ApiOperation(value = "文件下载", response = Result.class)
    public void download(@PathVariable("fileUUid") String fileUUid, HttpServletResponse response) throws IOException {
        File uploadFile = new File(fileUploadPath + fileUUid);
//      设置输出流格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUid, "UTF-8"));
        response.setContentType("application/octet-stream");

//      读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    @ApiOperation(value = "根据MD5查询文件", response = Result.class)
    public SysFile getFileByMD5(String md5){
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<SysFile> sysFileList = sysFileMapper.selectList(queryWrapper);
//      多个文件如果重复则返回第一个文件，否则返回null
        return sysFileList.size() == 0 ? null : sysFileList.get(0);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", response = Result.class)
    public Result<?> findAllByPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name){
        IPage<SysFile> page = new Page(pageNum,pageSize);
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
        queryWrapper.eq("is_delete",0);
        queryWrapper.orderByDesc("id");
        IPage<SysFile> fileIPage = sysFileService.page(page, queryWrapper);
        return Result.susscess(fileIPage);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新文件信息", response = Result.class)
    public Result<?> updateFile(@RequestBody SysFile sysFile){
        System.out.println("sysfile====>"+sysFile);
        boolean flag = sysFileService.updateById(sysFile);
        if(flag){
            return Result.susscess("更新成功");
        }else{
            return Result.error("-1","更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除文件信息", response = Result.class)
    public Result<?> delete(@PathVariable("id") Integer id) {
        SysFile sysFile = sysFileService.getById(id);
//      逻辑删除
        sysFile.setIsDelete(true);
        boolean flag = sysFileService.saveOrUpdate(sysFile);
        if(flag){
            return Result.susscess("删除成功");
        }else{
            return Result.error("-1","删除失败");
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除文件信息", response = Result.class)
    public Result<?> BatchDeleteFile(@RequestBody List<Integer> ids){
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<SysFile> sysFileList = sysFileMapper.selectList(queryWrapper);
        for (SysFile file : sysFileList) {
            file.setIsDelete(true);
            sysFileService.saveOrUpdate(file);
        }
        return Result.susscess("批量删除成功");
    }
}
