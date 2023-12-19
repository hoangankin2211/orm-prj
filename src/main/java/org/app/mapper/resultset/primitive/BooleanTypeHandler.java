package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class BooleanTypeHandler implements ITypeHandler<Boolean> {
  @Override
  public Boolean getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getBoolean(columnIndex);
  }
}