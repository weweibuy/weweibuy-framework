package com.weweibuy.framework.common.core.model.dto;

import lombok.Data;

/**
 * 通用分页请求
 *
 * @author durenhao
 * @date 2021/4/17 18:26
 **/
@Data
public class CommonPageRequest {

    /**
     * 页数
     */
    private Integer page;

    /**
     * 大小
     */
    private Integer size;

}
