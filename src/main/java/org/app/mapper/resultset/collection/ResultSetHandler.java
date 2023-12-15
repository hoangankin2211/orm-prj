package org.app.mapper.resultset.collection;

import org.app.mapper.ObjectMapperManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.resultset.primitive.TypeHandlerFactory;

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
    public List<T> getListResult( ResultSet resultSet) throws SQLException {
        List<T> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final T obj = getResult(resultSet, -1);
                result.add(obj);
            }
        } catch (Exception e) {
            throw new SQLException("Error: " + e.getMessage());
        }
        return result;
    }

    @Override
    public T getResult(ResultSet source, int index) throws SQLException {
        EntityMetaData entityMetaData = ObjectMapperManager.getInstance().getMapper(clazz);

        try {
            final T obj = clazz.getDeclaredConstructor().newInstance();
            final List<ColumnMetaData> columnMetaData = entityMetaData.getColumns();
            for (int i = 0; i < columnMetaData.size(); i++) {
                final ColumnMetaData column = columnMetaData.get(i);
                var typeHandler = TypeHandlerFactory.getInstance().getTypeHandler(column.getField().getType());
                Object object = typeHandler.getResult(source, i + 1);
                column.getField().set(obj, object);
            }
            return obj;

        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
