package com.weweibuy.framework.samples.state.biz.dto;

import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:17
 **/
@Data
@AllArgsConstructor
public class BillContext {
    private CmOrder cmOrder;
}
