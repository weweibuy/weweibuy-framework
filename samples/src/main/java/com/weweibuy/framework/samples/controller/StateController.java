package com.weweibuy.framework.samples.controller;

import com.weweibuy.framerwork.statemachine.core.StateMachineService;
import com.weweibuy.framework.samples.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import com.weweibuy.framework.samples.state.StateService;
import com.weweibuy.framework.samples.state.biz.BillSendFilterChainEntry;
import com.weweibuy.framework.samples.state.biz.DispatchBillFilter;
import com.weweibuy.framework.samples.state.biz.dto.BillContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Knight
 * @date : 2020/10/17 4:57 下午
 */
@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;
    private final StateMachineService stateMachineService;
    private final List<DispatchBillFilter> dispatchBillFilters;

    @GetMapping("/list")
    public ResponseEntity<String> state() {
        stateService.listState();
        return ResponseEntity.ok("teat ok");
    }

    @GetMapping("/machine/order")
    public ResponseEntity<Object> orderMachine(@RequestParam String event, @RequestParam String state, @RequestParam String orderNo) {
        return ResponseEntity.ok(stateMachineService.change(event, orderNo, state));
    }

    @PostMapping("/bill")
    public ResponseEntity<CommonCodeJsonResponse> createDispatchBill(@RequestBody CmOrder cmOrder) {
        BillSendFilterChainEntry entry = new BillSendFilterChainEntry(dispatchBillFilters);
        CommonCodeJsonResponse response = (CommonCodeJsonResponse) entry.doFilter(new BillContext(cmOrder));
        return ResponseEntity.ok(response);

    }


}
