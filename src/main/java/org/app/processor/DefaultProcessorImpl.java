package org.app.processor;

import lombok.Setter;
import org.app.datasource.DataSourceManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.executor.IQueryExecutor;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;

import java.util.ArrayList;
import java.util.List;

public class DefaultProcessorImpl<T, ID> implements IProcessor<T, ID> {

    @Setter
    private IQueryExecutor query;

    private final Class<T> clazz;

    private EntityMetaData metaData;

    public DefaultProcessorImpl(Class<T> clazz) {
        try {
            this.clazz = clazz;
            this.metaData = mapper.getMapper(clazz);
            this.query = DataSourceManager.getInstance().getQuery("default");
            query.create(metaData);
        } catch (Exception e) {
            throw new RuntimeException("Error: create table failed", e);
        }
    }

    public DefaultProcessorImpl(Class<T> clazz, String iQueryKey) {
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
                list.add(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public List<T> findAll() throws Exception {
        return query.select(metaData.getTableName(), clazz);
    }

    @Override
    public List<T> findBy(SelectClause selectClause, SpecificationClause specificationClause) throws Exception {
        return query.selectBy(metaData.getTableName(), selectClause, specificationClause, clazz);
    }

    @Override
    public List<T> findBy(SpecificationClause specificationClause) throws Exception {
        return findBy(null, specificationClause);
    }

    @Override
    public T findById(Object object) throws Exception {

        final ColumnMetaData primaryKey = metaData.getPrimaryKey();

        List<T> queryResult = query.selectBy(
                metaData.getTableName(),
                new SelectClause("*"),
                new CompareSpecification(primaryKey.getColumnName(), object),
                clazz
        );

        if (queryResult.isEmpty()) {
            return null;
        } else if (queryResult.size() > 1) {
            throw new RuntimeException("Error: more than one result");
        } else {
            return queryResult.get(0);
        }
    }


    @Override
    public T add(T obj) throws Exception {
        final List<ColumnMetaData> columnMetaData = metaData.getColumns();

        final List<Object> params = getColumnValList(columnMetaData, obj);

        int result = query.insert(metaData, params);

        if (result > 0) {
            return obj;
        } else {
            throw new RuntimeException("Error: insert failed");
        }
    }

    @Override
    public T update(T obj) throws Exception {
        var params = getColumnValList(metaData.getColumns(), obj);
        int result = query.update(metaData, params);

        if (result > 0) {
            return findById(params.get(0));
        }
        return null;
    }

    @Override
    public boolean delete(ID id) throws Exception {
        return query.delete(metaData, id);
    }
}
