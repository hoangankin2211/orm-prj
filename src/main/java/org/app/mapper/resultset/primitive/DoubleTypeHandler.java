package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DoubleTypeHandler implements ITypeHandler<Double> {
  @Override
  public Double getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getDouble(columnIndex);
  }
}
