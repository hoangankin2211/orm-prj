package org.app.query.executor;

import org.app.enums.CompareOperation;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.mapper.resultset.collection.ResultSetHandler;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.SpecificationClauseBuilder;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.app.utils.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultQueryExecutorImpl implements IQueryExecutor {
    private Connection connection;

    private final SqlUtils sqlUtils = SqlUtils.getInstances();

    @Override
    public void changeDataSourceConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ResultSet select(String tableName) {
        try {
            String statement = QueryBuilder.builder()
                    .selectAll()
                    .from(tableName)
                    .build();

            return preparedStatement(statement).executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private PreparedStatement preparedStatement(String statement) throws SQLException {
        try {
            return connection.prepareStatement(statement);
        } catch (SQLException e) {
            throw new SQLException(e);
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

        final ResultSet resultSet = preparedStatement(statement).executeQuery();
        return sqlUtils.handleResultSet(resultSet, clazz);
    }

    @Override
    public <T> List<T> selectBy(String tableName, ISpecification specificationClause, Class<T> clazz) throws Exception {
        final var _selectClause = new SelectClause("*");
        return selectBy(tableName, _selectClause, specificationClause, clazz);
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
            final ResultSet resultSet = preparedStatement(statement).executeQuery();

            final IResultSetHandler<T> resultSetHandler = new ResultSetHandler<>(clazz);
            return resultSetHandler.getListResult(resultSet);
        } catch (SQLException throwable) {
            throw new RuntimeException(throwable.getCause());
        }
    }

    @Override
    public boolean create(EntityMetaData entityMetaData) {
        try {
            final String statement = QueryBuilder.builder()
                    .create(entityMetaData)
                    .build();
            return execute(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insert(EntityMetaData entityMetaData, List<Object> params) throws Exception {
        return executeUpdate(
                QueryBuilder.builder()
                        .insert(entityMetaData.getTableName(), entityMetaData.getListColumns()).build(),
                params
        );
    }

    @Override
    public int update(String tableName, ISpecification setClause, ISpecification whereClause) throws Exception {
        return executeUpdate(
                QueryBuilder.builder()
                        .update(tableName, setClause)
                        .where(whereClause)
                        .build(),
                null
        );
    }

    @Override
    public boolean delete(String tableName, ISpecification whereClause) throws SQLException {
        return execute(
                QueryBuilder.builder()
                        .delete(tableName)
                        .where(whereClause)
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
    public boolean execute(String statement) throws SQLException {
        return preparedStatement(statement).execute();
    }

    @Override
    public int executeUpdate(String statement, List<Object> params) throws SQLException {
        final PreparedStatement preparedStatement = preparedStatement(statement);
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
        }
        return preparedStatement.executeUpdate();
    }


    @Override
    public ResultSet executeQuery(String statement) {
        final PreparedStatement preparedStatement;
        try {
            preparedStatement = preparedStatement(statement);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
