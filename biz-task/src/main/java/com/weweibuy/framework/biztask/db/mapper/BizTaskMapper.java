package com.weweibuy.framework.biztask.db.mapper;

import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.db.po.BizTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BizTaskMapper {
    long countByExample(BizTaskExample example);

    int deleteByExample(BizTaskExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BizTask record);

    int insertSelective(BizTask record);

    BizTask selectOneByExample(BizTaskExample example);

    List<BizTask> selectByExample(BizTaskExample example);

    BizTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BizTask record, @Param("example") BizTaskExample example);

    int updateByExample(@Param("record") BizTask record, @Param("example") BizTaskExample example);

    int updateByPrimaryKeySelective(BizTask record);

    int updateByPrimaryKey(BizTask record);
}