package org.app.mapper.resultset.primitive;


import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FloatTypeHandler implements ITypeHandler<Float> {
  @Override
  public Float getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getFloat(columnIndex);
  }
}
