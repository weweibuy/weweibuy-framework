package com.weweibuy.framework.compensate.mybatis.mapper;

import com.weweibuy.framework.compensate.mybatis.po.CompensateLog;
import com.weweibuy.framework.compensate.mybatis.po.CompensateLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompensateLogMapper {
    long countByExample(CompensateLogExample example);

    int deleteByExample(CompensateLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CompensateLog record);

    int insertSelective(CompensateLog record);

    CompensateLog selectOneByExample(CompensateLogExample example);

    List<CompensateLog> selectByExampleWithLimit(@Param("example") CompensateLogExample example, @Param("limit") Integer limit);

    List<CompensateLog> selectByExample(CompensateLogExample example);

    CompensateLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CompensateLog record, @Param("example") CompensateLogExample example);

    int updateByExample(@Param("record") CompensateLog record, @Param("example") CompensateLogExample example);

    int updateByPrimaryKeySelective(CompensateLog record);

    int updateByPrimaryKey(CompensateLog record);
}