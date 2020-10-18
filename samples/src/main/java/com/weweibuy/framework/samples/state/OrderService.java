package com.weweibuy.framework.samples.state;

import com.weweibuy.framework.samples.mybatis.plugin.mapper.CmOrderMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : Knight
 * @date : 2020/10/18 10:10 下午
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CmOrderMapper cmOrderMapper;


    public void createNumber(String orderStatus, String orderNo) {
        CmOrder order = new CmOrder();
        order.setOrderStatus(orderStatus);
        order.setOrderNo(orderNo);
        cmOrderMapper.insertSelective(order);
    }
}
