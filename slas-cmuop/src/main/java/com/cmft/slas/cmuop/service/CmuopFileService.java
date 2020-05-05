package com.cmft.slas.cmuop.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface CmuopFileService {

    void getFileInputStream(String fileName, String bucketName, HttpServletResponse response) throws Exception;

    String uploadImageFile(MultipartFile file, String bucketName, String userId, String typeName, String costumedFileName);

    String uploadAvatar(MultipartFile file, String bucketName, String userId);

    String uploadPdfFile(MultipartFile file, String bucketName, String userId, String costumedFileName);
}
