package org.app.query;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IQueryExecutor {

    void changeDataSourceConnection(Connection connection);


    <T> List<T> select(String tableName,Class<T> clazz) throws Exception;


    <T> List<T> selectBy(String tableName, ColumnMetaData[] column,Object[] params, Class<T> clazz) throws Exception;

    <T> List<T> select(String tableName, List<ColumnMetaData> columns, Class<T> clazz) throws Exception;
    <T> boolean create(EntityMetaData entityMetaData) throws Exception;


    <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception;

    <T> int update(EntityMetaData entityMetaData, List<Object> params) throws Exception;
    long count(final String tbName) throws SQLException;

    <T> boolean delete(EntityMetaData entityMetaData, Object id) throws SQLException;
}
