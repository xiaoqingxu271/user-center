package com.lc.usercenter.controller;

import com.lc.usercenter.common.BaseResponse;
import com.lc.usercenter.common.ResultUtils;
import com.lc.usercenter.exception.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author chun0
* @since 2026/3/6 13:28
* @version 1.0
*/
@RestController
@RequestMapping("/")
@Api(tags = "健康检查")
public class MainController {

    /**
     * 健康检查接口
     * @return 系统健康状态
     */
    @GetMapping("/health")
    @ApiOperation(value = "健康检查", notes = "返回系统健康状态")
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
