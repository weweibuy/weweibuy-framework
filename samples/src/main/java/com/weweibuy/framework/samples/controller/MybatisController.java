package com.weweibuy.framework.samples.controller;

import com.github.pagehelper.PageHelper;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.DBEncryptMapper;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.TbItemMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.DBEncryptExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.TbItemExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.DBEncrypt;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.TbItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.transaction.annotation.Transactional;
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

    private final TbItemMapper tbItemMapper;

    @GetMapping("/updatesql")
    public String updateSql() {
        TbItem tbItem = new TbItem();
        tbItem.setItemSn("item_sn_x");
        TbItemExample example = TbItemExample
                .newAndCreateCriteria()
                .andIdEqualTo(536563L)
                .andTitleLike("%xxx%")
                .example().orderBy();


        int i = tbItemMapper.updateByExampleSelective(tbItem, example);
        return i + "";
    }

    @GetMapping("/query")
    public Object query() {
        TbItemExample example = TbItemExample
                .newAndCreateCriteria()
                .andIdEqualTo(536563L)
                .andTitleLike("%卡xx%")
                .example();

        return tbItemMapper.selectByExample(example);
    }

}
