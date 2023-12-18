package org.app.query.executor;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.mapper.resultset.collection.ResultSetHandler;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.app.utils.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultQueryExecutorImpl implements IQueryExecutor {
    private Connection connection;

    private final SqlUtils sqlUtils = SqlUtils.getInstances();

    @Override
    public void changeDataSourceConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> List<T> select(String tableName, Class<T> clazz) {
        try {
            String statement = QueryBuilder.builder()
                    .selectAll()
                    .from(tableName)
                    .build();

            final ResultSet resultSet = preparedStatement(statement).executeQuery();

            final IResultSetHandler<T> resultSetHandler = new ResultSetHandler<>(clazz);

            return resultSetHandler.getListResult(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    private PreparedStatement preparedStatement(String statement) throws SQLException {
        try {
            return connection.prepareStatement(statement);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause, GroupByClause groupByClause, Class<T> clazz) throws Exception {
        final var _selectClause = selectClause == null ? new SelectClause("*") : selectClause;

        final QueryBuilder statement = QueryBuilder.builder()
                .select(_selectClause)
                .from(tableName)
                .where(specificationClause);

        if (groupByClause != null) {
            statement.groupBy(groupByClause);
        }

        System.out.println(statement);

        final ResultSet resultSet = preparedStatement(statement.build()).executeQuery();
        return sqlUtils.handleResultSet(resultSet, clazz);
    }

    @Override
    public <T> List<T> selectBy(String tableName, SelectClause selectClause, ISpecification specificationClause, Class<T> clazz) throws Exception {
        final var _selectClause = selectClause == null ? new SelectClause("*") : selectClause;

        final String statement = QueryBuilder.builder()
                .select(_selectClause)
                .from(tableName)
                .where(specificationClause)
                .build();

        System.out.println(statement);

        final ResultSet resultSet = preparedStatement(statement).executeQuery();
        return sqlUtils.handleResultSet(resultSet, clazz);
    }

    @Override
    public <T> List<T> selectBy(String tableName, ISpecification specificationClause, Class<T> clazz) throws Exception {
        final var _selectClause = new SelectClause("*");
        return selectBy(tableName, _selectClause, specificationClause, clazz);
    }

    public <T> List<T> selectByID(String tableName, EntityMetaData entityMetaData, Object id) throws Exception {

        final SpecificationClause specificationClause = SpecificationClause
                .builder()
                .addSpecification(
                        new CompareSpecification(entityMetaData.getPrimaryKey().getColumnName(), id)
                ).build();

        final String statement = QueryBuilder.builder()
                .select(new SelectClause("*"))
                .from(tableName)
                .where(specificationClause)
                .build();

        final ResultSet resultSet = preparedStatement(statement).executeQuery();
        final IResultSetHandler<T> resultSetHandler = new ResultSetHandler<>((Class<T>) entityMetaData.getClazz());

        return resultSetHandler.getListResult(resultSet);
    }

    @Override
    public <T> List<T> select(String tableName, List<ColumnMetaData> columns, Class<T> clazz) {
        String statement = QueryBuilder.builder()
                .select(
                        new SelectClause((String[]) columns.stream().map(ColumnMetaData::getColumnName).toArray())
                )
                .from(tableName)
                .build();

        try {
            final ResultSet resultSet = connection.prepareStatement(statement).executeQuery();

            final IResultSetHandler<T> resultSetHandler = new ResultSetHandler<>(clazz);
            return resultSetHandler.getListResult(resultSet);
        } catch (SQLException throwable) {
            throw new RuntimeException(throwable.getCause());
        }
    }

    @Override
    public <T> boolean create(EntityMetaData entityMetaData) {
        try {
            final String statement = QueryBuilder.builder()
                    .create(entityMetaData.getTableName(), entityMetaData.getColumns())
                    .build();
            return executeSql(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> int insert(EntityMetaData entityMetaData, List<Object> params) throws Exception {
        return executeSql(
                QueryBuilder.builder()
                        .insert(entityMetaData.getTableName(), entityMetaData.getColumns()).build(),
                params
        );
    }

    @Override
    public <T> int update(EntityMetaData entityMetaData, List<Object> params) throws Exception {
        SpecificationClause searchSpecification = new SpecificationClause();
        searchSpecification.addSpecification(new CompareSpecification(entityMetaData.getPrimaryKey().getColumnName(), params.get(0)));
        return executeSql(
                QueryBuilder.builder()
                        .update(entityMetaData.getTableName(), entityMetaData.getColumns())
                        .where(searchSpecification)
                        .build(),
                params.subList(1, params.size())
        );
    }

    @Override
    public boolean delete(EntityMetaData entityMetaData, Object id) throws SQLException {
        ColumnMetaData primaryKey = entityMetaData.getPrimaryKey();
        SpecificationClause searchSpecification = new SpecificationClause();
        searchSpecification.addSpecification(new CompareSpecification(primaryKey.getColumnName(), id));
        return executeSql(
                QueryBuilder.builder()
                        .delete(entityMetaData.getTableName())
                        .where(searchSpecification)
                        .build()
        );
    }

    @Override
    public long count(final String tbName) throws SQLException {
        final ResultSet resultSet = preparedStatement(
                QueryBuilder.builder()
                        .select(new SelectClause("COUNT(*) "))
                        .from(tbName)
                        .build()
        ).executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }


    @Override
    public boolean executeSql(String statement) throws SQLException {
        return preparedStatement(statement).execute();
    }

    @Override
    public int executeSql(String statement, List<Object> params) throws SQLException {
        final PreparedStatement preparedStatement = preparedStatement(statement);
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }
        return preparedStatement.executeUpdate();
    }

    @Override
    public <T> List<T> executeSql(String statement, Function<ResultSet, List<T>> function) throws SQLException {
        final PreparedStatement preparedStatement = preparedStatement(statement);
        ResultSet resultSet = preparedStatement.executeQuery();
        return function.apply(resultSet);
    }

    @Override
    public ResultSet executeSqlResultSet(String statement) throws SQLException {
        final PreparedStatement preparedStatement = preparedStatement(statement);
        return preparedStatement.executeQuery();
    }
}
