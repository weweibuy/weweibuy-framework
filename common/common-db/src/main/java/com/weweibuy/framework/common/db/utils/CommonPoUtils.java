package com.weweibuy.framework.common.db.utils;

import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import com.weweibuy.framework.common.db.model.CommonPo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 通用数据库模型工具
 *
 * @author durenhao
 * @date 2021/5/17 21:52
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonPoUtils {


    public static <T extends CommonPo> T clearCommonFiled(T commonPo) {
        commonPo.setId(null);
        commonPo.setDeleted(null);
        return clearTimestampFiled(commonPo);
    }

    public static <T extends CommonPo> T copyAndClear(T commonPo) {
        T copy = (T) BeanCopyUtils.copy(commonPo, commonPo.getClass());
        return clearCommonFiled(copy);
    }

    public static <T extends CommonPo> T clearTimestampFiled(T commonPo) {
        commonPo.setCreateTime(null);
        commonPo.setUpdateTime(null);
        return commonPo;
    }

    public static <T extends CommonPo> T copyAndClearTimestamp(T commonPo) {
        T copy = (T) BeanCopyUtils.copy(commonPo, commonPo.getClass());
        return clearTimestampFiled(copy);
    }

}
