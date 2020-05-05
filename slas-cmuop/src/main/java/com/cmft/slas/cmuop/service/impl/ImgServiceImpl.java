 package com.cmft.slas.cmuop.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmft.slas.ceph.client.CephClient;
import com.cmft.slas.cmuop.service.ImgService;
import com.cmft.slas.common.exception.BaseException;
import com.cmft.slas.common.utils.ImageTypeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImgServiceImpl implements ImgService {

    @Autowired
    private CephClient cephClient;

    @Value("${cmuop.img_path}")
    private String imgUrl;

    @Override
    public String uploadImg(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!ImageTypeUtil.contains(suffix.substring(1))) {
            return "上传文件格式错误！";
        }
        String uid = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        try {
            boolean uploadResult = cephClient.uploadWithInputStream(uid, file.getInputStream(), file.getSize());
            if (uploadResult) {
                String url = imgUrl + uid;
                return url;
            }
        } catch (IOException e) {
            String msg = String.format("图片上传失败:");
            log.error(msg, e);
            throw new BaseException(msg);
        }
        return "";
    }

    @Override
    public void downloadImg(String uid, HttpServletResponse response) throws Exception {
        byte[] image;
        image = cephClient.download(uid);
        OutputStream toClient = response.getOutputStream();
        String suffix = uid.substring(uid.lastIndexOf("."));
        response.setContentType(ImageTypeUtil.getMimeType(suffix.substring(1)));
        toClient.write(image);
        toClient.close();
    }

}
