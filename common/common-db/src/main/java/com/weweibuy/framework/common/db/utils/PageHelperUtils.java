package com.weweibuy.framework.common.db.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weweibuy.framework.common.core.model.dto.CommonPageRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * 分页插件工具
 *
 * @author durenhao
 * @date 2021/5/17 21:43
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageHelperUtils {

    public static <T> Page<T> startPage(CommonPageRequest commonPageRequest) {
        return PageHelper.startPage(Optional.ofNullable(commonPageRequest)
                        .map(CommonPageRequest::getPage)
                        .filter(i -> i > 0)
                        .orElse(1),
                Optional.ofNullable(commonPageRequest)
                        .map(CommonPageRequest::getSize)
                        .filter(i -> i > 0)
                        .orElse(10));
    }

    public static <T> Page<T> startPage(CommonPageRequest commonPageRequest, String order) {
        Page<T> page = startPage(commonPageRequest);
        page.setOrderBy(order);
        return page;
    }


}
