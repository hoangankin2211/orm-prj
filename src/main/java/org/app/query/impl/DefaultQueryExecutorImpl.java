package org.app.query.impl;

import lombok.ToString;
import org.app.mapper.ObjectMapperManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.IQueryExecutor;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.app.utils.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultQueryExecutorImpl implements IQueryExecutor {
    private Connection connection;

    private final SqlUtils sqlUtils = SqlUtils.getInstances();

    @Override
    public void changeDataSourceConnection(Connection connection) {
        this.connection = connection;
    }

    private PreparedStatement preparedStatement(String statement) throws SQLException {
        return connection.prepareStatement(statement);
    }

    @Override
    public <T> List<T> select(String tableName,Class<T> clazz){
        try {
            String statement = QueryBuilder.builder()
                    .selectAll()
                    .from(tableName)
                    .build();

            final ResultSet resultSet = preparedStatement(statement).executeQuery();
            return sqlUtils.handleResultSet(resultSet,clazz);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return  new ArrayList<>();
    }

    @Override
    public <T> List<T> selectBy(String tableName, ColumnMetaData[] column, Object[] params, Class<T> clazz) throws Exception{
        if (params.length != column.length){
            throw new RuntimeException("Error: params length not equal column length");
        }
        final SpecificationClause<T> specificationClause = new SpecificationClause<>();
        for (int i = 0 ;i< column.length;i++) {
            specificationClause.addSpecification(new SearchByIdSpecification<>(column[i], params[i]));
        }

        final String statement = QueryBuilder.builder()
                .select(new SelectClause("*"))
                .from(tableName)
                .where(specificationClause)
                .build();

        System.out.println(statement);

        final ResultSet resultSet = preparedStatement(statement).executeQuery();
        return sqlUtils.handleResultSet(resultSet,clazz);
    }

    private static final class SearchByIdSpecification<T> implements ISpecification<T> {
        private final ColumnMetaData columnMetaData;
        private final Object id;

        public SearchByIdSpecification(ColumnMetaData columnMetaData, Object id) {
            this.columnMetaData = columnMetaData;
            this.id = id;
        }

        @Override
        public String toString() {
            return columnMetaData.getColumnName() + " = " + id.toString();
        }

        @Override
        public boolean isSatisfiedBy(T entity) {
            try {
                return columnMetaData.getField().get(entity).equals(id);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public <T> List<T> select(String tableName,List<ColumnMetaData> columns,Class<T> clazz){
        String statement = QueryBuilder.builder()
                .select(
                        new SelectClause((String[])columns.stream().map(ColumnMetaData::getColumnName).toArray())
                )
                .from(tableName)
                .build();

        try {
            final ResultSet resultSet = connection.prepareStatement(statement).executeQuery();
            return sqlUtils.handleResultSet(resultSet,clazz);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  new ArrayList<>();
    }

    @Override
    public <T> boolean create(EntityMetaData entityMetaData) {
       try {
           final String statement = QueryBuilder.builder()
                   .create(entityMetaData.getTableName(), entityMetaData.getColumnMetaDataMap())
                   .build();
           return executeSql(statement);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }

    @Override
    public <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception {
        return executeSql(
                QueryBuilder.builder()
                    .insert(entityMetaData.getTableName(),entityMetaData.getColumnMetaDataMap()).build(),
                params
        );
    }

    @Override
    public <T> int update(EntityMetaData entityMetaData, List<Object> params) throws Exception {
        SpecificationClause<T> searchSpecification = new SpecificationClause<>();
        searchSpecification.addSpecification(new SearchByIdSpecification<>(entityMetaData.getColumnMetaDataMap().get(0),params.get(0)));
        return executeSql(
                QueryBuilder.builder()
                        .update(entityMetaData.getTableName(),entityMetaData.getColumnMetaDataMap())
                        .where(searchSpecification)
                        .build(),
                params.subList(1,params.size())
        );
    }

    @Override
    public <T> boolean delete(EntityMetaData entityMetaData, Object id) throws SQLException {
        SpecificationClause<T> searchSpecification = new SpecificationClause<>();
        searchSpecification.addSpecification(new SearchByIdSpecification<>(entityMetaData.getColumnMetaDataMap().get(0),id));
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


    private <T> boolean executeSql(String statement) throws SQLException {
        return connection.prepareStatement(statement).execute();
    }

    private  int executeSql(String statement,List<Object> params) throws SQLException {
        final PreparedStatement preparedStatement = preparedStatement(statement);
        for (int i = 0;i<params.size();i++){
            preparedStatement.setObject(i+1,params.get(i));
        }
       return preparedStatement.executeUpdate();
    }
}
