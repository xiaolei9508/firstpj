package com.cmft.slas.cmuop.common.utils;

import java.io.*;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DESEncryptUtil {

    private static final Logger logger = LoggerFactory.getLogger(DESEncryptUtil.class);

    /** 加密工具 */
    private Cipher encryptCipher = null;

    /** 解密工具 */
    private Cipher decryptCipher = null;
    // private static String keyVal;

    public DESEncryptUtil(String keyVal) {
        try {
            logger.info("创建解密文件工具对象");
            this.initialize_encryptKey(keyVal);
            this.initalize_dencryptkey(keyVal);
        } catch (Exception e) {
            logger.error("创建解密工具对象出错：" + ExceptionUtils.getStackTrace(e));
        }
    }

    private void initialize_encryptKey(String keyValue) throws Exception {
        Key key = getKey(keyValue.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public void initalize_dencryptkey(String keyValue) throws Exception {
        Key key = getKey(keyValue.getBytes());
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];

        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 解密字节数组
     * 
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 文件file进行加密并保存目标文件destFile中
     * 
     * @param sourceFileName 要加密的文件 如c:/test/srcFile.txt
     * @param diminationFileName 加密后存放的文件名 如c:/加密后文件.txt
     */
    public void encrypt(String sourceFileName, String diminationFileName) throws Exception {
        InputStream is = new FileInputStream(sourceFileName);
        OutputStream out = new FileOutputStream(diminationFileName);
        CipherInputStream cis = new CipherInputStream(is, encryptCipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > -1) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    public void encrypt(File sourceFile, File diminationFile) throws Exception {
        InputStream is = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(diminationFile);
        CipherInputStream cis = new CipherInputStream(is, encryptCipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = cis.read(buffer)) > -1) {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    /**
     * 文件采用DES算法解密文件
     * 
     * @param sourceFileName 已加密的文件 如c:/加密后文件.txt
     * @param diminationFileName 解密后存放的文件名 如c:/ test/解密后文件.txt
     */
    public boolean decrypt(String sourceFileName, String diminationFileName) {
        logger.info("开始解密文件，源文件路径为：{}，目标文件路径为：{}", sourceFileName, diminationFileName);
        boolean flag = false;
        InputStream is = null;
        OutputStream out = null;
        CipherOutputStream cos = null;
        try {
            is = new FileInputStream(sourceFileName);
            out = new FileOutputStream(diminationFileName);
            cos = new CipherOutputStream(out, decryptCipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            flag = true;
            logger.info("nuc组织文件解密成功");
        } catch (Exception e) {
            logger.error("解密文件发生错误：" + ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                if (null != cos) {
                    cos.close();
                }
                if (null != out) {
                    out.close();
                }
                if (null != is) {
                    is.close();
                }
            } catch (Exception e) {
                logger.error("解密文件关闭流出错：" + ExceptionUtils.getStackTrace(e));
            }
        }
        return flag;
    }

    public void decrypt(File sourceFile, File fileout) throws Exception {
        InputStream is = new FileInputStream(sourceFile);
        OutputStream out = new FileOutputStream(fileout);
        CipherOutputStream cos = new CipherOutputStream(out, decryptCipher);
        byte[] buffer = new byte[1024];
        int r;
        while ((r = is.read(buffer)) >= 0) {
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }

    /**
     * 加密字符串
     * 
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字符串
     * 
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * 
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用2个字符才能表示，所以字符串的长度是数组长度的2倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
     * 
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte)Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public static void main(String[] args) throws Exception {
        String keyVal = "9435b5c45a774db8b45042aea7b9c557";// 文件秘钥
        DESEncryptUtil desEncryptUtil = new DESEncryptUtil(keyVal);
        Long l = System.currentTimeMillis();
        System.out.println("开始" + l);
        // 文件加密解密
        desEncryptUtil.encrypt("D:\\workspace\\nucs\\organize-data-file\\ens.db",
            "D:\\workspace\\nucs\\organize-data-file\\des.db");// 加密
        System.out.println("结束" + (System.currentTimeMillis() - l));
        desEncryptUtil.decrypt("D:\\workspace\\nucs\\organize-data-file\\ens.db",
            "D:\\workspace\\nucs\\organize-data-file\\des.db");// 解密
        System.out.println("结束2" + (System.currentTimeMillis() - l));
        // 手机号加密解密
        String msg = "18100000000";
        System.out.println("加密前的字符：" + msg);
        String msg_ens = desEncryptUtil.encrypt(msg);
        System.out.println("加密后的字符：" + msg_ens);
        String msg_des = desEncryptUtil.decrypt(msg_ens);
        System.out.println("解密后的字符：" + msg_des);
    }
}
