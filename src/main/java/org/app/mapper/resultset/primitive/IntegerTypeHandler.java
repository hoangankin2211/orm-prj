package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class IntegerTypeHandler implements ITypeHandler<Integer> {
  @Override
  public Integer getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getInt(columnIndex);
  }
}
