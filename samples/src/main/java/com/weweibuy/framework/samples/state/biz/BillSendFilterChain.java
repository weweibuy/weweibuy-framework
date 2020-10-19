package com.weweibuy.framework.samples.state.biz;

import com.weweibuy.framework.samples.state.biz.dto.BillContext;

/**
 * @author zhang.suxing
 * @date 2020/10/19 22:15
 **/
public interface BillSendFilterChain {

    Object doFilter(BillContext context);
}
