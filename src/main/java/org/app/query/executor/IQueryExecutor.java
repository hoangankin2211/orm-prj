package org.app.query.executor;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SpecificationClause;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IQueryExecutor {

    void changeDataSourceConnection(Connection connection);
    <T> List<T> select(String tableName,Class<T> clazz) throws Exception;
    <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause, Class<T> clazz) throws Exception;

    <T> List<T> select(String tableName, List<ColumnMetaData> columns, Class<T> clazz) throws Exception;
    <T> boolean create(EntityMetaData entityMetaData) throws Exception;

    <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception;

    <T> int update(EntityMetaData entityMetaData, List<Object> params) throws Exception;

    long count(final String tbName) throws SQLException;

    <T> boolean delete(EntityMetaData entityMetaData,Object id) throws SQLException;
}
