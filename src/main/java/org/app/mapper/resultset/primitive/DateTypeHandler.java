package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public final class DateTypeHandler implements ITypeHandler<Date> {

  // 1900-1-1
  private static final Date DEFAULT = new Date(-2209017600000L);

  @Override
  public Date getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getDate(columnIndex);
  }

}
