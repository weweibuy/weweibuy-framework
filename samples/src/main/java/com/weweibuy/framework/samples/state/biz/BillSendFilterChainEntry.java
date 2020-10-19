package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:19
 **/
public class BillSendFilterChainEntry implements BillSendFilterChain {
    @Autowired
    private List<DispatchBillFilter> dispatchBillFilters;

    @Override
    public Object doFilter(BillContext context) {
        for (DispatchBillFilter dispatchBillFilter : dispatchBillFilters) {
            dispatchBillFilter.filter();
        }
        return doSendOrder(context);
    }

    /**
     * chain 的终结点
     */
    private Object doSendOrder(BillContext billContext) {
        return new Object();
    }

}
