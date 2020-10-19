package com.weweibuy.framework.samples.state.biz.dto;

import com.weweibuy.framework.samples.state.biz.DispatchBillFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:24
 **/
@Component
@Slf4j
public class DispatchBillCreatedFilter implements DispatchBillFilter {

    @Override
    public Object filter() {
        log.info("订单创建成功---返回上游单号");
        return null;
    }
}
