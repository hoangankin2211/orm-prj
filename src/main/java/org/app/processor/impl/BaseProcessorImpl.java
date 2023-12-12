package org.app.processor.impl;

import org.app.enums.OperationType;
import org.app.mapper.impl.ClassAdapter;
import org.app.mapper.impl.EntityMapper;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.processor.IProcessor;
import org.app.utils.SqlUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public  class BaseProcessorImpl implements IProcessor {
    private final Connection connection;

    private final SqlUtils sqlUtils = SqlUtils.getInstances();

    private final Map<Class<?>, EntityMapper> mappers = new ConcurrentHashMap<>();

    public BaseProcessorImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> List<T> select(Class<T> clazz) throws Exception {
        return null;
    }

    @Override
    public <T> int create(T obj) throws Exception {
        executeSql(createSqlStatement(OperationType.CREATE, obj));
        return 0;
    }

    @Override
    public <T> int insert(T obj) throws Exception {
         executeSql(createSqlStatement(OperationType.INSERT, obj));
        return -1;
    }

    @Override
    public <T> int update(T obj) throws Exception {
        return 0;
    }

    @Override
    public <T> int delete(T obj) throws Exception {
        return 0;
    }


    private <T> void executeSql(String statement) throws SQLException {
         connection.prepareStatement(statement).execute();
    }

    private <T> void executeSql(String statement,List<Object> params) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(statement);
        for (int i = 0;i<params.size();i++){
            preparedStatement.setObject(i+1,params.get(i));
        }
        preparedStatement.execute();
    }

    protected EntityMapper getMapper(Class<?> clazz) {
        EntityMapper mapper = mappers.get(clazz);

        if (mapper == null) {
            mapper = new ClassAdapter(clazz);
            mappers.put(clazz, mapper);
        }

        return mapper;
    }

    protected String createSqlStatement(OperationType handleType, Object object) throws SQLException {
        if (handleType == OperationType.SELECT) {
            return null;
        }
        if (object == null) {
            throw new SQLException("Error: object is null");
        }

        EntityMapper mapper = getMapper(object.getClass());
        List<ColumnMetaData> columnMetaData = mapper.getColumnMetaDataMap().values().stream().toList();

        if (handleType == OperationType.INSERT) {
            return insertSql(mapper.getTableName(), columnMetaData, object);
        } else if (handleType == OperationType.DELETE) {
        } else if (handleType == OperationType.UPDATE){
        }
        else if (handleType == OperationType.CREATE) {
            return createTableStatement(mapper.getTableName(), columnMetaData);
        }

        return "";
    }

    private String handleCreateSql( ColumnMetaData columnMetaData) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(columnMetaData.getColumnName());
        sqlBuilder.append(" ");
        sqlBuilder.append(sqlUtils.getSqlType(columnMetaData.getType().getClass()));
        if (columnMetaData.isPrimaryKey()) {
            sqlBuilder.append(" PRIMARY KEY");
        }
        return sqlBuilder.toString();
    }

    private String createTableStatement(String tbName,List<ColumnMetaData> columnMetaData) {
        return "create table " +
                tbName +
                columnMetaData
                        .stream()
                        .map(this::handleCreateSql)
                        .collect(Collectors.joining(",", "(", ")")) +
                ";" + System.lineSeparator();
    }


    private String insertSql(final String tbName, List<ColumnMetaData> columnMetaData,Object object) {
        final var listValue = getColumnValList(columnMetaData,object);
        return "insert into " + tbName +
                columnMetaData.stream().map(ColumnMetaData::getColumnName)
                        .collect(Collectors.joining(",", "(", ")")) +
                " values " +
                Collections.nCopies(columnMetaData.size(), "?").stream().collect(Collectors.joining(",", "(", ")")) +
                ";" + System.lineSeparator();
    }

    private List<Object> getColumnValList(List<ColumnMetaData> columnMetaData, Object o) {
        List<Object> list = new ArrayList<>();
        for (ColumnMetaData column : columnMetaData) {
            try {
                Object data = column.getField().get(o);
                if (data != null) {
                    list.add(data);
                }
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }
        return list;
    }

}
