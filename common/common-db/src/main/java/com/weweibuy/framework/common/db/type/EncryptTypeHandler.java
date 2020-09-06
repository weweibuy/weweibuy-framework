package com.weweibuy.framework.common.db.type;

import com.weweibuy.framework.common.db.encrypt.AESEncryptHelper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据加解密处理工具
 *
 * @author durenhao
 * @date 2020/9/5 11:59
 **/
public class EncryptTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, AESEncryptHelper.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return encrypted(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return encrypted(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return encrypted(cs.getString(columnIndex));
    }


    private String encrypted(String content) {
        return content == null ? null : (AESEncryptHelper.isEncrypted(content) ?
                AESEncryptHelper.decrypt(content) : content);
    }

}
