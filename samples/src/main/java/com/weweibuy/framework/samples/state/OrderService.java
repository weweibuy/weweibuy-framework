package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.common.enums.OrderState;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.CmOrderMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : Knight
 * @date : 2020/10/18 10:10 下午
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final CmOrderMapper cmOrderMapper;


    public Object createNumber(String orderNO, String orderStatus) {
        log.info("订单创建----目标状态" + orderStatus);
        CmOrder order = new CmOrder();
        order.setOrderStatus(orderStatus);
        order.setOrderNo(orderNO);
        cmOrderMapper.insertSelective(order);
        return CommonDataResponse.success(OrderDto.builder()
                .orderNo(orderNO)
                .orderStatus(OrderState.INITIALED.name())
                .build());
    }
}
