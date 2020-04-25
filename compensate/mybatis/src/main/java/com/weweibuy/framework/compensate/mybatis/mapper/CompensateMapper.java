package com.weweibuy.framework.compensate.mybatis.mapper;

import com.weweibuy.framework.compensate.mybatis.po.Compensate;
import com.weweibuy.framework.compensate.mybatis.po.CompensateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompensateMapper {
    long countByExample(CompensateExample example);

    int deleteByExample(CompensateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Compensate record);

    int insertSelective(Compensate record);

    Compensate selectOneByExample(CompensateExample example);

    List<Compensate> selectByExample(CompensateExample example);

    Compensate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Compensate record, @Param("example") CompensateExample example);

    int updateByExample(@Param("record") Compensate record, @Param("example") CompensateExample example);

    int updateByPrimaryKeySelective(Compensate record);

    int updateByPrimaryKey(Compensate record);
}