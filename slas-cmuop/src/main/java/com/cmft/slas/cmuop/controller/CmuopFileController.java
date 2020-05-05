package com.cmft.slas.cmuop.controller;

import com.cmft.slas.cmuop.dto.UserDTO;
import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.service.CmuopFileService;
import com.cmft.slas.cmuop.service.UserService;
import com.cmft.slas.common.pojo.WebResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author liurp001
 * @Since 2020/1/9
 */
@RestController
@RequestMapping("/cmuop")
public class CmuopFileController {

    @Autowired
    private CmuopFileService cmuopFileService;

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*")
    @GetMapping("/file/download/{uid:.*}")
    public void getImage(@PathVariable("uid") String uid,
                         @RequestParam(required = false) String bucket,
                         HttpServletResponse response) {
        try {
            cmuopFileService.getFileInputStream(uid, bucket, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/file/upload")
    public WebResponse<String> uploadImgFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam(required = false) String bucketName,
                                             @RequestParam(required = false) String userId,
                                             @RequestParam(required = false) String typeName,
                                             @RequestParam(required = false) String costumedFileName) {
        String result = cmuopFileService.uploadImageFile(file, bucketName, userId, typeName, costumedFileName);
        return StringUtils.isBlank(result) ? WebResponse.error("上传图片失败", "") : WebResponse.success(result, "图片上传成功");
    }

    @PostMapping("/avatar/upload")
    public WebResponse<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false) String userId,
                                            @RequestParam(required = false) String bucketName) {
        String imgUrl = cmuopFileService.uploadAvatar(file, bucketName, userId);
        if(StringUtils.isNotBlank(imgUrl)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUid(userId);
            userDTO.setImgUrl(imgUrl);
            userService.updateUserByUid(userDTO);
            return WebResponse.success(imgUrl, "头像上传成功");
        }
        return WebResponse.error("上传图头像失败", "");
    }

    @PostMapping("/file/upload/pdf")
    public WebResponse<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam(required = false) String bucketName,
                                             @RequestParam(required = false) String userId,
                                             @RequestParam(required = false) String costumedFileName) {
        String result = cmuopFileService.uploadPdfFile(file, bucketName, userId, costumedFileName);
        return StringUtils.isBlank(result) ? WebResponse.error("上传文件失败", "") : WebResponse.success(result, "文件上传成功");
    }

}
