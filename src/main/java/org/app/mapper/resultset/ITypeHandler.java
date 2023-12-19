package org.app.mapper.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITypeHandler<TO> {
    TO getResult(ResultSet source, int index) throws SQLException;
}
