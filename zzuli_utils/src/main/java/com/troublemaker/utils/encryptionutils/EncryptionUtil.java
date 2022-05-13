package com.troublemaker.utils.encryptionutils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.AES;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static cn.hutool.crypto.Mode.ECB;
import static cn.hutool.crypto.Padding.ZeroPadding;

/**
 * @author Troublemaker
 * @date 2022- 04 24 10:03
 */
public class EncryptionUtil {
    public static String getAesToUrl(String username, String url) {
        String address = null;
        String key = "1234567887654321";
        byte[] bytes = key.getBytes();
        AES aes = new AES(ECB, ZeroPadding, bytes);
        String encode = Base64.encode(aes.encrypt(username));
        try {
            address = URLEncoder.encode(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url + address;
    }

    public static String getBase64Password(String password) {
        byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
        return "{base64}_" + java.util.Base64.getEncoder().encodeToString(bytes);
    }
}
