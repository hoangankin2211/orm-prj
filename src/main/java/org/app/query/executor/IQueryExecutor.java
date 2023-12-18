package org.app.query.executor;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SpecificationClause;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IQueryExecutor {

    void changeDataSourceConnection(Connection connection);
    <T> List<T> select(String tableName,Class<T> clazz) throws Exception;
    <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause,GroupByClause groupByClause ,  Class<T> clazz) throws Exception;
    <T> List<T> selectBy(String tableName, SelectClause selectClause,ISpecification specificationClause, Class<T> clazz) throws Exception;
    <T> List<T> selectBy(String tableName, ISpecification specificationClause, Class<T> clazz) throws Exception;
    <T> List<T> select(String tableName, List<ColumnMetaData> columns, Class<T> clazz) throws Exception;
    <T> boolean create(EntityMetaData entityMetaData) throws Exception;

    <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception;

    <T> int update(EntityMetaData entityMetaData, List<Object> params) throws Exception;

    long count(final String tbName) throws SQLException;

    boolean delete(EntityMetaData entityMetaData,Object id) throws SQLException;
     boolean executeSql(String statement) throws SQLException;

    int executeSql(String statement, List<Object> params) throws SQLException ;
    <T> List<T> executeSql(String statement, Function<ResultSet, List<T>> function) throws SQLException ;
    ResultSet executeSqlResultSet(String statement) throws SQLException ;
}
