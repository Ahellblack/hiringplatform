package com.siti.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密128位CBC模式工具类
 */
public class AESUtil {
	
    //解密密钥(自行随机生成)
    public static final String KEY = "sitiits@sari@cai";//密钥key
    public static final String IV  = "kindness@its@tao";//向量iv
	
    //加密
    public static String Encrypt(String content) throws Exception {  
        byte[] raw = KEY.getBytes("utf-8");  
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度    
        IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);  
        byte[] encrypted = cipher.doFinal(content.getBytes());  
        return new BASE64Encoder().encode(encrypted);
    }  
	
    //解密  
    public static String Decrypt(String content) throws Exception {  
        try {   
            byte[] raw = KEY.getBytes("utf-8");  
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);  
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);
            try {  
                byte[] original = cipher.doFinal(encrypted1);  
                String originalString = new String(original);  
                return originalString;  
            } catch (Exception e) {  
                System.out.println(e.toString());  
                return null;  
            }  
        } catch (Exception ex) {  
            System.out.println(ex.toString());  
            return null;  
        }  
    } 
	
    public static void main(String[] args) throws Exception {
    	String content = "商城县赤城办事处金刚台大道西段9号商城县人民医院";
    	//加密
    	String ens = AESUtil.Encrypt(content);
    	System.out.println("加密后：" + ens);
    	//解密  ""
        String des = AESUtil.Decrypt("t5OyTK6XJ/nU/tPeCRNfC+k59I8HeVRobf8juf00KD/majt8psdeDOUTWNGAKwOTKlmYQVkAmetG\n+LCny+WLrNUuvZox4N67xbwNFtcpe6M=");
        System.out.println("解密后：" + des);
    }
 
}
