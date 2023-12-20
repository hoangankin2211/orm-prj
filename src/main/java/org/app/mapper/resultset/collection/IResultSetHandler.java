package org.app.mapper.resultset.collection;

import org.app.mapper.resultset.ITypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IResultSetHandler<TO> extends ITypeHandler<TO> {
     List<TO> getListResult(ResultSet resultSet);
}
