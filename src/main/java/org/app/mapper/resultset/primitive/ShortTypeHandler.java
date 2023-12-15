package org.app.mapper.resultset.primitive;


import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ShortTypeHandler implements ITypeHandler<Short> {

  @Override
  public Short getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getShort(columnIndex);
  }

}
