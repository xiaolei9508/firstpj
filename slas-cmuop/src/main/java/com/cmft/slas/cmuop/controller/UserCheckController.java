package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.service.UserCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.common.pojo.WebResponse;

import io.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@RequestMapping("/app")
public class UserCheckController {

    @Autowired
    private UserCheckService userCheckService;

    @PostMapping("/userCheck")
    @ApiOperation(value = "检查用户", notes = "检查用户", produces = "application/json")
    public WebResponse<String> checkUserStatus(@RequestBody UserDTO userDTO) {
       String body = userCheckService.checkUser(userDTO);
       return WebResponse.success(body, body);
    }
}
