package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ByteTypeHandler implements ITypeHandler<Byte> {
  @Override
  public Byte getResult(final ResultSet rs, final int columnIndex) throws SQLException {
    return rs.getByte(columnIndex);
  }
}
