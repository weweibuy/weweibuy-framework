package com.weweibuy.framework.compensate.mybatis.mapper;

import com.weweibuy.framework.compensate.mybatis.po.CompensateMethodArgsExt;
import com.weweibuy.framework.compensate.mybatis.po.CompensateMethodArgsExtExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompensateMethodArgsExtMapper {
    long countByExample(CompensateMethodArgsExtExample example);

    int deleteByExample(CompensateMethodArgsExtExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CompensateMethodArgsExt record);

    int insertSelective(CompensateMethodArgsExt record);

    CompensateMethodArgsExt selectOneByExample(CompensateMethodArgsExtExample example);

    List<CompensateMethodArgsExt> selectByExampleWithLimit(@Param("example") CompensateMethodArgsExtExample example, @Param("limit") Integer limit);

    List<CompensateMethodArgsExt> selectByExample(CompensateMethodArgsExtExample example);

    CompensateMethodArgsExt selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CompensateMethodArgsExt record, @Param("example") CompensateMethodArgsExtExample example);

    int updateByExample(@Param("record") CompensateMethodArgsExt record, @Param("example") CompensateMethodArgsExtExample example);

    int updateByPrimaryKeySelective(CompensateMethodArgsExt record);

    int updateByPrimaryKey(CompensateMethodArgsExt record);
}