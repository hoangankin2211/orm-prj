package org.app.query.executor;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SpecificationClause;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IQueryExecutor {

    void changeDataSourceConnection(Connection connection);

    ResultSet select(String tableName) throws Exception;

    <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause, GroupByClause groupByClause, Class<T> clazz) throws Exception;

    <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause, Class<T> clazz) throws Exception;

    <T> List<T> selectBy(String tableName, ISpecification specificationClause, Class<T> clazz) throws Exception;

    <T> List<T> select(String tableName, List<ColumnMetaData> columns, Class<T> clazz) throws Exception;

    boolean create(EntityMetaData entityMetaData) throws Exception;


    int insert(EntityMetaData entityMetaData, List<Object> params) throws Exception;

    int update(String tableName, ISpecification setClause, ISpecification whereClause) throws Exception;

    boolean delete(String tableName, ISpecification whereClause) throws SQLException;

    long count(final String tbName) throws SQLException;

    boolean execute(String statement) throws SQLException;

    int executeUpdate(String statement, List<Object> params) throws SQLException;

    ResultSet executeQuery(String statement) throws SQLException;
}
