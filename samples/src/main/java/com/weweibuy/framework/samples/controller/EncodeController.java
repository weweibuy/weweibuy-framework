package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.utils.AESUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author durenhao
 * @date 2020/9/5 11:33
 **/
@RestController
@RequestMapping("/encode")
public class EncodeController {

    @GetMapping("/key")
    public String key() {
        return AESUtils.generateKeyHex();
    }


    @GetMapping("/encrypt")
    public String encode(String pw, String src) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        SecretKey key = AESUtils.createKey(pw);
        return AESUtils.encrypt(key, src);
    }

    @GetMapping("/decrypt")
    public String decrypt(String pw, String src) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        SecretKey key = AESUtils.createKey(pw);
        return AESUtils.decrypt(key, src);
    }


}
