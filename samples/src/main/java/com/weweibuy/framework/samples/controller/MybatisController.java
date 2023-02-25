package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.mybatis.plugin.mapper.DBEncryptMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.DBEncryptExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.DBEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2023/2/25 22:11
 **/
@RestController
@RequestMapping("/mybatis")
@RequiredArgsConstructor
public class MybatisController {

    private final DBEncryptMapper dbEncryptMapper;

    @GetMapping("/updatesql")
    public String updateSql() {
        DBEncrypt dbEncrypt = new DBEncrypt();
        DBEncryptExample example = DBEncryptExample
                .newAndCreateCriteria()
                .andIdEqualTo(1L)
                .example()
                .updateSql("create_time = now()",
                        "update_time = '2022-12-12 00:00:00'"
                        );

        int i = dbEncryptMapper.updateByExampleSelective(dbEncrypt, example);
        return i + "";
    }


}
