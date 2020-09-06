package com.weweibuy.framework.common.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageResult<T> {

    private static final CommonPageResult EMPTY = new CommonPageResult(0L, Collections.emptyList());

    private Long total;

    private List<T> list;

    @SuppressWarnings("unchecked")
    public static final <T> CommonPageResult<T> empty() {
        return (CommonPageResult<T>) EMPTY;
    }

    public static final <T> CommonPageResult<T> withTotalAndList(Long total, List<T> list) {
        return new CommonPageResult(total, list);
    }

}
