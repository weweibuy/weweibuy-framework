package com.weweibuy.framework.samples.mybatis.plugin.mapper;

import com.weweibuy.framework.samples.mybatis.plugin.model.example.DBEncryptExample;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.DBEncrypt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DBEncryptMapper {
    long countByExample(DBEncryptExample example);

    int deleteByExample(DBEncryptExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DBEncrypt record);

    int insertSelective(DBEncrypt record);

    DBEncrypt selectOneByExample(DBEncryptExample example);

    DBEncrypt selectOneByExampleForUpdate(DBEncryptExample example);

    List<DBEncrypt> selectByExampleWithLimit(@Param("example") DBEncryptExample example, @Param("limit") Integer limit);

    List<DBEncrypt> selectByExampleForUpdate(DBEncryptExample example);

    List<DBEncrypt> selectByExample(DBEncryptExample example);

    DBEncrypt selectByPrimaryKeyForUpdate(Long id);

    DBEncrypt selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DBEncrypt record, @Param("example") DBEncryptExample example);

    int updateByExample(@Param("record") DBEncrypt record, @Param("example") DBEncryptExample example);

    int updateByPrimaryKeySelective(DBEncrypt record);

    int updateByPrimaryKey(DBEncrypt record);
}