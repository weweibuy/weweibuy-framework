package com.weweibuy.framework.samples.mybatis.plugin.mapper;

import com.weweibuy.framework.samples.mybatis.plugin.model.example.RouteFilterArgsExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.RouteFilterArgs;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RouteFilterArgsMapper {
    long countByExample(RouteFilterArgsExample example);

    int deleteByExample(RouteFilterArgsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RouteFilterArgs record);

    int insertSelective(RouteFilterArgs record);

    RouteFilterArgs selectOneByExample(RouteFilterArgsExample example);

    RouteFilterArgs selectOneByExampleForUpdate(RouteFilterArgsExample example);

    List<RouteFilterArgs> selectByExampleForUpdate(RouteFilterArgsExample example);

    List<RouteFilterArgs> selectByExample(RouteFilterArgsExample example);

    RouteFilterArgs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RouteFilterArgs record, @Param("example") RouteFilterArgsExample example);

    int updateByExample(@Param("record") RouteFilterArgs record, @Param("example") RouteFilterArgsExample example);

    int updateByPrimaryKeySelective(RouteFilterArgs record);

    int updateByPrimaryKey(RouteFilterArgs record);
}