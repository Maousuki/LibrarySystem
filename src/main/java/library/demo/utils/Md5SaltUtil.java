package library.demo.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import library.demo.pojo.User;
import org.apache.tomcat.util.codec.binary.Base64;

/*
 * 实现MD5加盐加密与解密
 */
public class Md5SaltUtil {
    private static MessageDigest md5;//md5加密对象
    private static Base64 base64;//加密编码对象
    private static String salt = "";//盐

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");//获取MD5加密对象
            base64 = new Base64();//获取加密编码对象
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /*
     * 对password进行MD5加盐加密，返回加密过的password,并存储盐salt
     */
    public static String encrypt(String username, String password) {
        String lastPassword = "";
        salt = username;
        String passwordSalt = password + salt;//将密码和盐拼接到一起
        try {
            md5.reset();//初始化
            md5.update(passwordSalt.getBytes("UTF-8"));//将加盐密码传给消息摘要对象
            byte[] bys = md5.digest();//创建消息摘要对象
            byte[] lastPasswordStr = base64.encode(bys);//进行加密
            lastPassword = new String(lastPasswordStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return lastPassword;
    }

}

