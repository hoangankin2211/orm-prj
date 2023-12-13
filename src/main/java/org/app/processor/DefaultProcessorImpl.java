package org.app.processor;

import lombok.Setter;
import org.app.datasource.DataSourceManager;
import org.app.mapper.ObjectMapperManager;
import org.app.mapper.adapter.EntityObjectAdapter;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.IQuery;
import org.app.query.impl.DefaultQueryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultProcessorImpl<T> implements IProcessor<T> {
    @Setter
    private  IQuery query;

    private final Class<?> clazz;

    public DefaultProcessorImpl(Class<?> clazz) {
        try {
            this.query = DataSourceManager.getInstance().getQuery("default");
            query.create(mapper.getMapper(clazz));
        } catch (Exception e) {
            throw new RuntimeException("Error: create table failed",e);
        }
        this.clazz = clazz;
    }

    public DefaultProcessorImpl(Class<?> clazz,String iQueryKey) {
        try {
            this.query =  DataSourceManager.getInstance().getQuery(iQueryKey);
            query.create(mapper.getMapper(clazz));
        } catch (Exception e) {
            throw new RuntimeException("Error: create table failed",e);
        }
        this.clazz = clazz;
    }


    private List<Object> getColumnValList(List<ColumnMetaData> columnMetaData,Object object) {
        List<Object> list = new ArrayList<>();
        for (ColumnMetaData column : columnMetaData) {
            try {
                Object data = column.getField().get(object);
                if (data != null) {
                    list.add(data);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public List<T> findAll() throws Exception {
        return (List<T>) query.select(mapper.getMapper(clazz).getTableName(),clazz);
    }


    @Override
    public T add(T obj) throws Exception {
        final var entityMetaData = mapper.getMapper(obj.getClass());

        final List<ColumnMetaData> columnMetaData = entityMetaData
                .getColumnMetaDataMap();

        final List<Object> params = getColumnValList(columnMetaData, obj);

        params.set(0, ((DefaultQueryImpl) query).count(entityMetaData.getTableName()));

        int result = query.insert(entityMetaData,params);

        if (result > 0) {
            return obj;
        }
        else {
            throw new RuntimeException("Error: insert failed");
        }
    }

    @Override
    public T update(T obj) throws Exception {
        return null;
    }

    @Override
    public T delete(T obj) throws Exception {
        return null;
    }
}
