package com.weweibuy.framework.common.db.model;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2021/5/17 21:27
 **/
public interface CommonPo {

    Long getId();

    void setId(Long id);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);


}
