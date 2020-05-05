package com.cmft.slas.cmuop.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.cmft.slas.cmuop.entity.User;
import com.cmft.slas.cmuop.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmft.slas.ceph.client.CephClient;
import com.cmft.slas.cmuop.service.CmuopFileService;
import com.cmft.slas.common.exception.BaseException;
import com.cmft.slas.common.utils.ImageTypeUtil;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;

@Slf4j
@Service
public class CmuopFileServiceImpl implements CmuopFileService {

    @Autowired
    private CephClient cephClient;

    @Autowired
    private UserMapper userMapper;

    @Value("${cmuop.img_path}")
    private String filePath;

    @Value("${cmuop.avatar.prefix}")
    private String avatarPrefix;

    @Value("${cmuop.avatar.default}")
    private String defaultAvatar;

    @Value("${cmuop.avatar.identifier}")
    private String identifier;

    @Override
    public void getFileInputStream(String fileName, String bucketName, HttpServletResponse response) throws Exception {
        OutputStream toClient = response.getOutputStream();
        byte[] image;
        /*
         * should the avatar file not exists in ceph (which must contains the avatar prefix),
         * return default avatart file stream
         */
        if (fileName.contains(avatarPrefix) && !cephClient.fileExists(fileName)) {
            image = cephClient.download(defaultAvatar);
        } else {
            image = StringUtils.isBlank(bucketName) ? cephClient.download(fileName)
                : cephClient.download(fileName, bucketName);
        }
        // response.setContentType("application/octet-stream");
        // String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // String processedSuffix = StringUtils.isBlank(suffix) ? "png" : suffix;
        String[] splitName = fileName.split("\\.");
        String suffix = splitName.length < 2 ? "png" : splitName[1];
        response.setContentType(ImageTypeUtil.getMimeType(suffix));
        response.addHeader("Cache-control", "max-age=2592000");
        toClient.write(image);
        toClient.close();
    }

    /**
     * should the image's name is specified, use the name regardless CAUTIOUS: OVERRIDING ISSUE!!!!!!!
     */
    @Override
    public String uploadImageFile(MultipartFile file, String bucketName, String userId, String typeName,
        String costumedFileName) {
        log.debug("开始上传图片originalFileName[{}], bucketName:[{}], userId:[{}]", file.getOriginalFilename(), bucketName,
            userId);
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        if (!ImageTypeUtil.contains(suffix.substring(1))) {
            throw new BaseException("图片格式错误");
        }
        String fileName;
        if (StringUtils.isNotBlank(costumedFileName)) {
            fileName = costumedFileName;
        } else {
            fileName = generateFileName(userId, typeName, suffix);
        }
        try {
            boolean uplaodResult = uploadFile(bucketName, fileName, file);
            if (uplaodResult) {
                log.debug("成功上传图片originalFileName[{}], bucketName:[{}], userId:[{}], \nurl:{}",
                    file.getOriginalFilename(), bucketName, userId, filePath + fileName);
                return filePath + fileName;
            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        return null;
    }

    @Override
    public String uploadAvatar(MultipartFile file, String bucketName, String userId) {
        log.debug("开始上传图片originalFileName[{}], bucketName:[{}], userId:[{}]", file.getOriginalFilename(), bucketName,
                userId);
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        if (!ImageTypeUtil.contains(suffix.substring(1))) {
            throw new BaseException("图片格式错误");
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String fileName = avatarPrefix + identifier
                + uuid + "_"
                + (StringUtils.isBlank(originalFileName) ? suffix : originalFileName);

        if (StringUtils.isNotBlank(bucketName))
            fileName += ("?bucket=" + bucketName);
        try {
            boolean uplaodResult = uploadFile(bucketName, fileName, file);
            if (uplaodResult) {
                log.debug("成功上传图片originalFileName[{}], bucketName:[{}], userId:[{}], \nurl:{}",
                    file.getOriginalFilename(), bucketName, userId, filePath + fileName);
                return filePath + fileName;
            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        return null;
    }

    @Override
    public String uploadPdfFile(MultipartFile file, String bucketName, String userId, String costumedFileName) {
        log.debug("开始上传文件 originalFileName[{}], bucketName:[{}], userId:[{}], costumedFileName[{}]",
                file.getOriginalFilename(), bucketName, userId, costumedFileName);
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        if (!"pdf".equals(suffix.substring(1))) {
            throw new BaseException("文件格式错误");
        }
        String fileName;
        if (StringUtils.isNotBlank(costumedFileName)) {
            fileName = costumedFileName;
        } else {
            fileName = generateFileName(userId, null, suffix);
        }
        try {
            boolean uplaodResult = uploadFile(bucketName, fileName, file);
            if (uplaodResult) {
                log.debug("成功上传文件originalFileName[{}], bucketName:[{}], userId:[{}], \nurl:{}",
                        file.getOriginalFilename(), bucketName, userId, filePath + fileName);
                return filePath + fileName;
            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        return null;
    }

    private Boolean uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException {
        boolean uplaodResult = false;
        if (StringUtils.isBlank(bucketName)) {
            uplaodResult = cephClient.uploadWithInputStream(fileName, file.getInputStream(), file.getSize());
        } else {
            uplaodResult =
                cephClient.uploadWithInputStream(fileName, bucketName, file.getInputStream(), file.getSize());
        }
        return uplaodResult;
    }

    private String generateFileName(String userId, String typeName, String suffix) {
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        if (StringUtils.isNotBlank(userId)) {
            fileName = userId + "_" + fileName;
        }
        if (StringUtils.isNotBlank(typeName)) {
            fileName = typeName + "_" + fileName;
        }
        return fileName;
    }

}
