package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.codec.aes.AESUtils;
import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.DBEncryptMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.DBEncryptExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.DBEncrypt;
import org.apache.commons.lang3.StringUtils;
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

    private final DBEncryptMapper dbEncryptMapper;

    public EncodeController(DBEncryptMapper dbEncryptMapper) {
        this.dbEncryptMapper = dbEncryptMapper;
    }

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

    @GetMapping("/db/insert")
    public Object insert(String phone, String idNo) {
        DBEncrypt dbEncrypt = new DBEncrypt();
        dbEncrypt.setIdNo(idNo);
        dbEncrypt.setPhone(phone);
        dbEncrypt.setOrderNo(IdWorker.nextStringId());
        dbEncryptMapper.insertSelective(dbEncrypt);
        return dbEncrypt;
    }

    @GetMapping("/db/select")
    public Object select(String phone, String idNo, String orderNo) {
        DBEncryptExample.Criteria criteria = DBEncryptExample.newAndCreateCriteria();
        if (StringUtils.isNotBlank(phone)) {
            criteria.andPhoneEqualTo(phone);
        }

        if (StringUtils.isNotBlank(idNo)) {
            criteria.andIdNoEqualTo(idNo);
        }

        if (StringUtils.isNotBlank(orderNo)) {
            criteria.andOrderNoEqualTo(orderNo);
        }
        return dbEncryptMapper.selectByExample(criteria.example());
    }


}
