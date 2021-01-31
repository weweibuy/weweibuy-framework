package com.weweibuy.framework.common.db.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
public class BCryptEncryptTypeHandler extends BaseTypeHandler<String> {

    public static final BCryptPasswordEncoder ENCODER_INSTANCE =
            new BCryptPasswordEncoder();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, ENCODER_INSTANCE.encode(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }


    public static boolean match(CharSequence rawPassword, String encodedPassword) {
        return ENCODER_INSTANCE.matches(rawPassword, encodedPassword);
    }

}
