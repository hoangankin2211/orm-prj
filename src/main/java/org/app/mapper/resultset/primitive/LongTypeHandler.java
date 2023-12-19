package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler implements ITypeHandler<Long> {
    @Override
    public Long getResult(ResultSet source, int index) throws SQLException {
        return source.getLong(index);
    }
}
