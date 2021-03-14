package com.weweibuy.framework.common.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 分页响应
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageResult<T> {

    private Long total;

    private List<T> list;

    public static final <T> CommonPageResult<T> empty() {
        return new CommonPageResult<>(0L, Collections.emptyList());
    }

    public static final <T> CommonPageResult<T> withTotalAndList(Long total, List<T> list) {
        return new CommonPageResult<>(total, list);
    }

    public static final <T> CommonPageResult<T> withTotalAndEmpty(Long total) {
        return new CommonPageResult<>(total, Collections.emptyList());
    }


}
