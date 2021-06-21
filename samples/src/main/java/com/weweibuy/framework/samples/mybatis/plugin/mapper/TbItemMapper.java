package com.weweibuy.framework.samples.mybatis.plugin.mapper;

import com.weweibuy.framework.samples.mybatis.plugin.model.example.TbItemExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.TbItem;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TbItemMapper {
    long countByExample(TbItemExample example);

    int deleteByExample(TbItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItem record);

    int insertSelective(TbItem record);

    TbItem selectOneByExample(TbItemExample example);

    TbItem selectOneByExampleForUpdate(TbItemExample example);

    List<TbItem> selectByExampleWithLimit(@Param("example") TbItemExample example, @Param("limit") Integer limit);

    List<TbItem> selectByExampleForUpdate(TbItemExample example);

    List<TbItem> selectByExample(TbItemExample example);

    TbItem selectByPrimaryKeyForUpdate(Long id);

    TbItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByExample(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByPrimaryKeySelective(TbItem record);

    int updateByPrimaryKey(TbItem record);
}