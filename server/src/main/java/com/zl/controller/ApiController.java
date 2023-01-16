package com.zl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "标准演示接口")
public class ApiController {
    @Resource
    private ObjectMapper mapper;

    @PostMapping("/ps")
    @ApiOperation(value = "接受json参数", notes = "演示json参数是否接受成功")
    public String post(@ApiParam(name = "接收json参数", defaultValue = "{}")
                       @RequestBody String json) throws IOException {
        Map map = mapper.readValue(json, Map.class);
        System.out.println(map);
        return json;
    }

}
