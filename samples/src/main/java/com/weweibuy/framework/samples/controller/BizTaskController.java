package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.support.BizTaskHelper;
import com.weweibuy.framework.common.core.model.dto.CommonDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2024/1/21 15:39
 **/
@RestController
@RequestMapping("/biz-task")
@RequiredArgsConstructor
public class BizTaskController {


    @GetMapping("/create")
    public CommonDataResponse<BizTask> create() {
        BizTask bizTask = BizTaskHelper.bizTaskBuilder()
                .bizId(1L)
                .taskType("test")
                .bizId(1L)
                .bizStatus(1)
                .taskParam("taskParam")
                .buildAndSave();
        return CommonDataResponse.success(bizTask);
    }

    @GetMapping("/transformer")
    public CommonDataResponse<BizTask> create(Long id) {

        BizTask bizTask = BizTaskHelper.query(id)
                .orElse(null);
        BizTaskHelper.bizTaskTransformer(bizTask)
                .newBizId(2L)
                .newTaskType("test2")
                .transformerTask();

        return CommonDataResponse.success(bizTask);
    }



}
