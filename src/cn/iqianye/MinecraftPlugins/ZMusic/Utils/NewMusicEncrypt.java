package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

/**
 * 最终版本！！！
 */
public class NewMusicEncrypt {

    // 密钥
    private static String nonce = "0CoJUm6Qyw8W8jud";

    // 偏移量
    private static String ivParameter = "0102030405060708";

    // 公共密钥
    private static String pubKey = "010001";

    // 基本指数
    private static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";

    /**
     * 随机字符串
     *
     * @param length 长度：取16
     * @return
     */
    public static String createSecretKey(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机字符串（无大写）
     *
     * @param length
     * @return
     */
    public static String createRandomKey(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param s 字符串
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * RSA 加密采用非常规填充方式，既不是PKCS1也不是PKCS1_OAEP，网易的做法是直接向前补0
     *
     * @param str
     * @param size
     * @return
     */
    public static String zfill(String str, int size) {
        while (str.length() < size) str = "0" + str;
        return str;
    }

    /**
     * AES加密
     * 此处使用AES-128-CBC加密模式，key需要为16位
     *
     * @param content 加密内容
     * @param sKey    偏移量
     * @return
     */
    public static String aesEncrypt(String content, String sKey) throws Exception {
        byte[] encryptedBytes;
        byte[] byteContent = content.getBytes("UTF-8");
        // 获取cipher对象，getInstance("算法/工作模式/填充模式")
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 采用AES方式将密码转化成密钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
        // 初始化偏移量
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
        // cipher对象初始化 init（“加密/解密,密钥，偏移量”）
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
        // 数据处理
        encryptedBytes = cipher.doFinal(byteContent);
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用
        return new String(Base64.getEncoder().encode(encryptedBytes), "UTF-8");
    }

    /**
     * RSA 加密
     *
     * @param secKey 随机16位字符串
     * @return
     */
    public static String rsaEncrypt(String secKey) {
        // 需要先将加密的消息翻转，再进行加密
        secKey = new StringBuffer(secKey).reverse().toString();
        // 转十六进制字符串
        String secKeyHex = stringToHexString(secKey);
        // 指定基数的数值字符串转换为BigInteger表示形式
        BigInteger biText = new BigInteger(secKeyHex, 16);
        BigInteger biEx = new BigInteger(pubKey, 16);
        BigInteger biMod = new BigInteger(modulus, 16);
        // 次方并求余（biText^biEx mod biMod is ?）
        BigInteger bigInteger = biText.modPow(biEx, biMod);
        return zfill(bigInteger.toString(16), 256);
    }

    /**
     * **获取加密参数**
     *
     * @param content 加密内容
     * @return
     * @throws Exception
     */
    public static HashMap<String, String> getData(String content) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        String secKey = createSecretKey(16);
        // 二次AES加密、加密模式都是CBC加密
        // 第一次加密使用content和nonce进行加密
        // 第二次使用第一次加密结果和16位随机字符串进行加密
        String params = aesEncrypt((aesEncrypt(content, nonce)), secKey);
        // RSA 加密
        String encSecKey = rsaEncrypt(secKey);
        data.put("params", params);
        data.put("encSecKey", encSecKey);
        return data;
    }

}
