package com.zl.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zl.common.Constants;
import com.zl.common.Result;
import com.zl.dto.UserDto;
import com.zl.mapper.UserMapper;
import com.zl.pojo.User;
import com.zl.service.UserService;
import com.zl.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", response = Result.class)
    public Result<?> login(@RequestBody UserDto userDto){
        if("".equals(userDto.getUsername()) || "".equals(userDto.getPassword())){
            return Result.error(Constants.CODE_400.getCode(),"登录失败，参数不可以有空值");
        }
       return Result.susscess(userService.login(userDto));
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册", response = Result.class)
    public Result<?> register(@RequestBody UserDto userDto){
        if("".equals(userDto.getUsername()) || "".equals(userDto.getPassword())){
            return Result.error(Constants.CODE_400.getCode(),"注册失败，参数不可以有空值");
        }
        return Result.susscess(userService.register(userDto));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", response = Result.class)
    public Result<?> findAllByPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String username,
                                   @RequestParam(defaultValue = "") String email,
                                   @RequestParam(defaultValue = "") String address){
        IPage<User> page = new Page(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username),"username",username);
        queryWrapper.like(StrUtil.isNotBlank(email),"email",email);
        queryWrapper.like(StrUtil.isNotBlank(address),"address",address);
        queryWrapper.orderByDesc("id");

//      通过token获取当前用户信息
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("currentUser=============>"+currentUser);

        IPage<User> userIPage = userService.page(page, queryWrapper);
        return Result.susscess(userIPage);
    }

    @GetMapping("/username/{username}")
    @ApiOperation(value = "个人信息查询", response = Result.class)
    public Result<?> queryUser(@PathVariable("username") String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        return Result.susscess(user);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增用户信息", response = Result.class)
    public Result<?> addUser(@RequestBody User user){
        System.out.println(user);
        user.setPassword("123456");
        boolean flag = userService.save(user);
        if(flag){
            return Result.susscess("新增成功");
        }else{
            return Result.error("-1","新增失败");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新用户信息", response = Result.class)
    public Result<?> updateUser(@RequestBody User user){
        boolean flag = userService.updateById(user);
        if(flag){
            return Result.susscess("更新成功");
        }else{
            return Result.error("-1","更新失败");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除用户信息", response = Result.class)
    public Result<?> delete(@PathVariable("id") Integer id) {
        boolean flag = userService.removeById(id);
        if(flag){
            return Result.susscess("删除成功");
        }else{
            return Result.error("-1","删除失败");
        }
    }

    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除用户信息", response = Result.class)
    public Result<?> BatchDeleteUser(@RequestBody List<Integer> ids){
        System.out.println("ids=====>"+ids);
        boolean flag = userService.deleteIds(ids);
        if(flag){
            return Result.susscess("批量删除成功");
        }else{
            return Result.error("-1","批量删除失败");
        }
    }


//    @GetMapping("/page2")
//    @ApiOperation(value = "分页查询", response = Result.class)
//    public Result<?> findAllByPage2(@RequestParam Integer pageNum,
//                                   @RequestParam Integer pageSize,
//                                   @RequestParam(defaultValue = "") String username,
//                                   @RequestParam(defaultValue = "") String nickname,
//                                   @RequestParam(defaultValue = "") String address){
//        pageNum = (pageNum - 1)*pageSize;
////        List<User> userList = userMapper.selectPage(pageNum, pageSize,username);
//        Integer total = userMapper.selectTotal(username);
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("total",total);
////        hashMap.put("userList",userList);
//        return Result.susscess(hashMap);
//    }


    @GetMapping("/export")
    @ApiOperation(value = "数据导出")
    public void export(HttpServletResponse response) throws IOException {
        //先从数据库查出所有的数据
        List<User> list = userService.list();
        //我们创建Hutool给我们写好的ExcelWrite
        ExcelWriter wirter = ExcelUtil.getWriter(true);
        wirter.write(list,true);
        //我们将输出的excel文件写出到客户端直接下载
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        /*设置导出文件名称*/
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");
        ServletOutputStream out=response.getOutputStream();
        wirter.flush(out,true);
        //关闭wirter，释放内存
        wirter.close();
        //关机输出servlet流
        IoUtil.close(out);
    }

    @PostMapping("/import")
    @ApiOperation(value = "数据导入", response = Result.class)
    public Result<?> imp(MultipartFile file) throws IOException {
        //获取来自浏览器发送的文件内容
        InputStream inputStream = file.getInputStream();
        //这里我们不用磁盘路径这种写死的，我们使用输入流动态进行读取
//        ExcelReader reader = ExcelUtil.getReader("读取文件来源");
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
        //我们再使用MP的批量插入插入到数据库中即可
        boolean flag = userService.saveBatch(users);
        if(flag){
            return Result.susscess("数据导入成功");
        }else{
            return Result.error("-1","数据导入失败");
        }
    }


}
