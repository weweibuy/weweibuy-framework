package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:24
 **/
@Slf4j
@Component
public class DispatchBillCreatedFilter implements DispatchBillFilter {

    @Override
    public int order() {
        return 2;
    }

    @Override
    public Object filter(BillContext context) {
        log.info("订单创建成功---返回上游单号");
        return context;
    }
}
