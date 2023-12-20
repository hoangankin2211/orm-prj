package org.app.mapper.resultset.collection;

import org.app.mapper.ObjectMapperManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.resultset.TypeHandlerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ResultSetHandler<T> implements IResultSetHandler<T> {
    private final Class<T> clazz;
    public ResultSetHandler(Class<T> clazz) {
        this.clazz = clazz;
    }
    @Override
    public  List<T> getListResult( ResultSet resultSet) throws SQLException {
        List<T> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final T obj = getResult(resultSet, -1);
                result.add(obj);
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return result;
    }

    @Override
    public T getResult(ResultSet source, int index) throws SQLException {
        try {
            EntityMetaData entityMetaData = ObjectMapperManager.getInstance().getMapper(clazz);
            final T obj = clazz.getDeclaredConstructor().newInstance();
            final Map<String,ColumnMetaData> columnMetaData = entityMetaData.getColumnsMap();
            for (int i = 0; i < source.getMetaData().getColumnCount(); i++) {

                final ColumnMetaData column = columnMetaData.get(source.getMetaData().getColumnName(i + 1));

                if (column == null){
                    throw new RuntimeException("Can not found column name " + source.getMetaData().getColumnName(i + 1));
                }

                var typeHandler = TypeHandlerFactory.getInstance().getTypeHandler(column.getField().getType());
                Object object = typeHandler.getResult(source, i + 1);
                column.getField().set(obj, object);
            }
            return obj;

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            if (e instanceof NoSuchMethodException) {
                throw new RuntimeException("No default constructor found for " + clazz.getName() + ". Please add a default constructor for this class.");
            }
            throw new RuntimeException(e);
        }
    }
}
