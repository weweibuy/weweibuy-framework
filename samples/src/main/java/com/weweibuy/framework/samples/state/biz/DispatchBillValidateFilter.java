package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhang.suxing
 * @date 2020/10/20 20:36
 **/
@Slf4j
@Component
public class DispatchBillValidateFilter implements DispatchBillFilter {

    @Override
    public int order() {
        return 1;
    }

    @Override
    public Object filter(BillContext billContext) {
        log.info("订单开始校验---");
        return billContext;
    }
}
