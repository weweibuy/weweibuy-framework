package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.common.enums.OrderState;
import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.CmOrderMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.example.CmOrderExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @author : Knight
 * @date : 2020/10/18 10:10 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final CmOrderMapper cmOrderMapper;

    Object createOrder(String orderNo, String orderStatus) {
        log.info("订单创建----目标状态" + orderStatus);
        CmOrder order = new CmOrder();
        order.setOrderStatus(orderStatus);
        order.setOrderNo(orderNo);
        cmOrderMapper.insertSelective(order);
        return CommonDataResponse.success(OrderDto.builder()
                .orderNo(orderNo)
                .orderStatus(OrderState.INITIALED.name())
                .build());
    }

    Object deliveryOrder(String orderNo, String orderStatus) {
        //step1:校验当前order业务状态
        CmOrderExample example = new CmOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        CmOrder cm = Optional.ofNullable(cmOrderMapper.selectOneByExample(example)).orElseThrow(
                () -> {
                    throw new BusinessException(CommonCodeResponse.unknownException());
                }
        );
        cm.setOrderStatus(orderStatus);
        cmOrderMapper.updateByExample(cm, example);
        return CommonDataResponse.success(OrderDto.builder()
                .orderNo(orderNo)
                .orderStatus(OrderState.INITIALED.name())
                .build());
    }
}
