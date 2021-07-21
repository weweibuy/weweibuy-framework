package com.weweibuy.framework.common.db.model;

import java.time.LocalDateTime;

/**
 * 通用的数据库对象
 *
 * @author durenhao
 * @date 2021/5/17 21:27
 **/
public interface CommonPo {

    default Long getId() {
        return null;
    }

    default void setId(Long id) {

    }

    default Boolean getDeleted() {
        return null;
    }

    default void setDeleted(Boolean deleted) {

    }

    default LocalDateTime getCreateTime() {
        return null;
    }

    default void setCreateTime(LocalDateTime createTime) {

    }

    default LocalDateTime getUpdateTime() {
        return null;
    }

    default void setUpdateTime(LocalDateTime updateTime) {

    }


}
