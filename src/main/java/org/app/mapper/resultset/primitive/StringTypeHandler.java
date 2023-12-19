package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class StringTypeHandler implements ITypeHandler<String> {

  private static final String DEFAULT = "";

  @Override
  public String getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getString(columnIndex);
  }

}
