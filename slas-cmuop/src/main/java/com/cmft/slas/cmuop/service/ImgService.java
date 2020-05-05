 package com.cmft.slas.cmuop.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface ImgService {

    String uploadImg(MultipartFile file);

    void downloadImg(String uid, HttpServletResponse response) throws Exception;
}
