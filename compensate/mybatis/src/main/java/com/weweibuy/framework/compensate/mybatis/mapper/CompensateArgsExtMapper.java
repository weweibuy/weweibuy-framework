package com.weweibuy.framework.compensate.mybatis.mapper;

import com.weweibuy.framework.compensate.mybatis.po.CompensateArgsExt;
import com.weweibuy.framework.compensate.mybatis.po.CompensateArgsExtExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CompensateArgsExtMapper {
    long countByExample(CompensateArgsExtExample example);

    int deleteByExample(CompensateArgsExtExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CompensateArgsExt record);

    int insertSelective(CompensateArgsExt record);

    CompensateArgsExt selectOneByExample(CompensateArgsExtExample example);

    List<CompensateArgsExt> selectByExampleWithLimit(@Param("example") CompensateArgsExtExample example, @Param("limit") Integer limit);

    List<CompensateArgsExt> selectByExample(CompensateArgsExtExample example);

    CompensateArgsExt selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CompensateArgsExt record, @Param("example") CompensateArgsExtExample example);

    int updateByExample(@Param("record") CompensateArgsExt record, @Param("example") CompensateArgsExtExample example);

    int updateByPrimaryKeySelective(CompensateArgsExt record);

    int updateByPrimaryKey(CompensateArgsExt record);
}