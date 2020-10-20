package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:19
 **/
public class BillSendFilterChainEntry implements BillSendFilterChain {
    @Autowired
    private List<DispatchBillFilter> dispatchBillFilters;

    public BillSendFilterChainEntry(List<DispatchBillFilter> dispatchBillFilters) {
        this.dispatchBillFilters = dispatchBillFilters.stream()
                .sorted(Comparator.comparingInt(DispatchBillFilter::order))
                .collect(Collectors.toList());
    }

    @Override
    public Object doFilter(BillContext context) {
        for (DispatchBillFilter dispatchBillFilter : dispatchBillFilters) {
            dispatchBillFilter.filter(context);
        }
        return doSendOrder(context);
    }

    /**
     * chain 的终结点
     */
    private Object doSendOrder(BillContext billContext) {
        return CommonCodeJsonResponse.success();
    }

}
