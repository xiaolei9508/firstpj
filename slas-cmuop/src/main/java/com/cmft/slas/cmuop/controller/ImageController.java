package com.cmft.slas.cmuop.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cmft.slas.cmuop.service.ImgService;
import com.cmft.slas.common.pojo.WebResponse;

import io.swagger.annotations.ApiOperation;

/**
 * @Author liurp001
 * @Since 2019/12/30
 */
@RestController("/headline/cmuop/admin/upload")
public class ImageController {

    @Autowired
    private ImgService imgService;

    @ApiOperation(value = "uploading thumbnail")
    @PostMapping("/image")
    public WebResponse uploadThumbnail(@RequestParam MultipartFile file) {
        String uid = imgService.uploadImg(file);
        return uid.equals("") ? WebResponse.success(uid) : WebResponse.error("上传失败");
    }

    @ApiOperation(value = "uploading thumbnail")
    @GetMapping("/image/{uid:.*}")
    public void downloadThumbnail(@PathVariable("uid") String uid, HttpServletResponse response) {
        try {
            imgService.downloadImg(uid, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
