package com.weweibuy.framework.samples.mybatis.plugin.mapper;

import com.weweibuy.framework.samples.mybatis.plugin.model.example.CmOrderExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.CmOrder;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CmOrderMapper {
    long countByExample(CmOrderExample example);

    int deleteByExample(CmOrderExample example);

    int insert(CmOrder record);

    int insertSelective(CmOrder record);

    CmOrder selectOneByExample(CmOrderExample example);

    CmOrder selectOneByExampleForUpdate(CmOrderExample example);

    List<CmOrder> selectByExampleForUpdate(CmOrderExample example);

    List<CmOrder> selectByExample(CmOrderExample example);

    int updateByExampleSelective(@Param("record") CmOrder record, @Param("example") CmOrderExample example);

    int updateByExample(@Param("record") CmOrder record, @Param("example") CmOrderExample example);
}