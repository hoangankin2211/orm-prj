package org.app.query.impl;

import org.app.enums.OperationType;
import org.app.mapper.ObjectMapperManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.IQueryExecutor;
import org.app.utils.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultQueryExecutorImpl implements IQueryExecutor {
    private Connection connection;

    private final SqlUtils sqlUtils = SqlUtils.getInstances();


    @Override
    public void changeDataSourceConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> List<T> select(String tableName,Class<T> clazz){
        String statement = "select * from " + tableName + ";";
        try {
            final ResultSet resultSet = connection.prepareStatement(statement).executeQuery();
            return handleResultSet(resultSet,clazz);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  new ArrayList<>();
    }

    private <T> List<T> handleResultSet(ResultSet resultSet,Class<T> clazz) {
        try {
            final List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                final T obj = clazz.newInstance();
                final EntityMetaData entityMetaData = ObjectMapperManager.getInstance().getMapper(clazz);
                final List<ColumnMetaData> columnMetaData = entityMetaData.getColumnMetaDataMap();
                for (int i = 0; i < columnMetaData.size(); i++) {

                    final ColumnMetaData column = columnMetaData.get(i);
                    var object = resultSet.getObject(i + 1);
                    column.getField().set(obj, object);

                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultQueryExecutorImpl() {
    }

    @Override
    public <T> boolean create(EntityMetaData entityMetaData) {
       try {
           final String statement = createSqlStatement(OperationType.CREATE,entityMetaData);
           return executeSql(statement);
       }catch (Exception e){
           throw new RuntimeException(e);
       }
    }

    @Override
    public <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception {

        return executeSql(createSqlStatement(OperationType.INSERT, entityMetaData),params);
    }

    @Override
    public <T> int update(T obj) throws Exception {
        return 0;
    }

    @Override
    public <T> int delete(T obj) throws Exception {
        return 0;
    }

    public long count(final String tbName) throws SQLException {
        final ResultSet resultSet = connection.prepareStatement(countNumRow(tbName)).executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }


    private <T> boolean executeSql(String statement) throws SQLException {
        return connection.prepareStatement(statement).execute();
    }

    private  int executeSql(String statement,List<Object> params) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (int i = 0;i<params.size();i++){
            preparedStatement.setObject(i+1,params.get(i));
        }
       return preparedStatement.executeUpdate();
    }



    protected String createSqlStatement(OperationType handleType, EntityMetaData entityMetaData) throws SQLException {
        if (handleType == OperationType.SELECT) {
            select(entityMetaData.getTableName(),entityMetaData.getClazz());
        }

        if (entityMetaData == null) {
            throw new SQLException("Error: object is null");
        }


        List<ColumnMetaData> columnMetaData = entityMetaData.getColumnMetaDataMap();

        if (handleType == OperationType.INSERT) {
            return insertSql(entityMetaData.getTableName(),columnMetaData);
        } else if (handleType == OperationType.DELETE) {
            return "";
        } else if (handleType == OperationType.UPDATE){
            return "";
        }
        else if (handleType == OperationType.CREATE) {
            return createTableStatement(entityMetaData.getTableName(), columnMetaData);
        }

        throw new SQLException("Error: handleType is null");
    }

    private String handleCreateSql( ColumnMetaData columnMetaData) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(columnMetaData.getColumnName());
        sqlBuilder.append(" ");
        sqlBuilder.append(sqlUtils.getSqlType(columnMetaData.getType()));
        if (columnMetaData.isPrimaryKey()) {
            sqlBuilder.append(" PRIMARY KEY");
        }
        return sqlBuilder.toString();
    }

    private String createTableStatement(String tbName,List<ColumnMetaData> columnMetaData) {

        String statement =  "create table if not exists " +
                tbName +
                columnMetaData
                        .stream()
                        .map(this::handleCreateSql)
                        .collect(Collectors.joining(",", "(", ")")) +
                ";" + System.lineSeparator();

        System.out.println(statement);

        return statement;
    }


    private String insertSql(final String tbName, List<ColumnMetaData> columnMetaData) {
        if (columnMetaData.isEmpty()){
            throw new RuntimeException("Error: columnMetaData is empty");
        }

        return "insert into " + tbName +
                columnMetaData.stream()
                        .map(ColumnMetaData::getColumnName)
                        .collect(Collectors.joining(",", "(", ")")) +
                " values " +
                Collections.nCopies(columnMetaData.size()  , "?").stream().collect(Collectors.joining(",", "(", ")")) +
                ";" + System.lineSeparator();
    }

    private String countNumRow(final String tbName) {
        return "SELECT count(*) FROM "  + tbName + ";";
    }



}
