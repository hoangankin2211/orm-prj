package org.app.processor;

import lombok.Setter;
import org.app.annotations.Entity;
import org.app.annotations.Id;
import org.app.datasource.DataSourceManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.IQueryExecutor;
import org.app.query.impl.DefaultQueryExecutorImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultProcessorImpl<T> implements IProcessor<T> {

    @Setter
    private IQueryExecutor query;

    private final Class<?> clazz;

    public DefaultProcessorImpl(Class<?> clazz) {
        try {
            this.query = DataSourceManager.getInstance().getQuery("default");
            query.create(mapper.getMapper(clazz));
        } catch (Exception e) {
            throw new RuntimeException("Error: create table failed", e);
        }
        this.clazz = clazz;
    }

    public DefaultProcessorImpl(Class<?> clazz, String iQueryKey) {
        try {
            this.query = DataSourceManager.getInstance().getQuery(iQueryKey);
            query.create(mapper.getMapper(clazz));
        } catch (Exception e) {
            throw new RuntimeException("Error: create table failed", e);
        }
        this.clazz = clazz;
    }


    private List<Object> getColumnValList(List<ColumnMetaData> columnMetaData, Object object) throws RuntimeException {
        List<Object> list = new ArrayList<>();
        for (ColumnMetaData column : columnMetaData) {
            try {
                Object data = column.getField().get(object);
//                if (column.isPrimaryKey()) {
//                    if (!column.isAutoIncrement()) {
//                        list.add(data);
//                    }
//                } else {
                list.add(data);
//                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public List<T> findAll() throws Exception {
        return (List<T>) query.select(mapper.getMapper(clazz).getTableName(), clazz);
    }

    @Override
    public T findById(Object object) throws Exception {
        final EntityMetaData entityMetaData = mapper.getMapper(clazz);

        final ColumnMetaData primaryKey = entityMetaData.getColumnMetaDataMap().get(0);

        return (T) query.selectBy(
                entityMetaData.getTableName(),
                new ColumnMetaData[]{primaryKey},
                new Object[]{object},
                clazz).get(0);
    }

    @Override
    public T add(T obj) throws Exception {
        final var entityMetaData = mapper.getMapper(obj.getClass());

        final List<ColumnMetaData> columnMetaData = entityMetaData.getColumnMetaDataMap();

        final List<Object> params = getColumnValList(columnMetaData, obj);

        int result = query.insert(entityMetaData, params);

        if (result > 0) {
            return obj;
        } else {
            throw new RuntimeException("Error: insert failed");
        }
    }

    @Override
    public T update(T obj) throws Exception {
        final var entityMetaData = mapper.getMapper(obj.getClass());
        var params = getColumnValList(entityMetaData.getColumnMetaDataMap(), obj);
        int result = query.update(entityMetaData, params);

        final T updated = (T) query.selectBy(entityMetaData.getTableName(),
                new ColumnMetaData[]{entityMetaData.getColumnMetaDataMap().get(0)},
                new Object[]{params.get(0)},
                obj.getClass()
        ).get(0);

        if (result > 0) {
            return updated;
        } else {
            throw new RuntimeException("Error: update failed");
        }
    }

    @Override
    public boolean delete(T obj) throws Exception {
        final var entityMetaData = mapper.getMapper(obj.getClass());
        final Object id = entityMetaData.getColumnMetaDataMap().get(0).getField().get(obj);
        return query.delete(entityMetaData,id);
    }
}
