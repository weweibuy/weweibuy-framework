package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhang.suxing
 * @date 2020/10/20 20:33
 **/
@Component
@Slf4j
public class DispatchBillTaskFilter implements DispatchBillFilter {

    @Override
    public Object filter(BillContext context) {
        log.info("订单下发任务生成----");
        return context;
    }

    @Override
    public int order() {
        return 3;
    }
}
