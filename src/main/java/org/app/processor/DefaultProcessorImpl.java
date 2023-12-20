package org.app.processor;

import lombok.Setter;
import org.app.datasource.DataSourceManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.resultset.TypeHandlerFactory;
import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.mapper.resultset.collection.ResultSetHandler;
import org.app.query.executor.IQueryExecutor;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.EqualSpecification;
import org.app.query.specification.impl.SetClause;
import org.app.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.app.datasource.DataSourceManager.DEFAULT_QUERY;

public class DefaultProcessorImpl<T, ID> implements IProcessor<T, ID> {

    @Setter
    protected IQueryExecutor query;
    protected final Class<T> clazz;
    protected EntityMetaData metaData;
    protected final TypeHandlerFactory typeHandlerFactory = TypeHandlerFactory.getInstance();
    protected IResultSetHandler<T> resultSetHandler;

    public DefaultProcessorImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.metaData = mapper.getMapper(clazz);
        this.query = DataSourceManager.getInstance().getQuery(DEFAULT_QUERY);
        typeHandlerFactory.registerTypeHandler(clazz, new ResultSetHandler<>(clazz));
        resultSetHandler = typeHandlerFactory.getResultSetTypeHandler(clazz);
    }

    public DefaultProcessorImpl(Class<T> clazz, String iQueryKey) {
        this.query = DataSourceManager.getInstance().getQuery(iQueryKey);
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
    public List<T> findAll() {
        final ResultSet resultSet;
        try {
            resultSet = query.select(metaData.getTableName());
            return resultSetHandler.getListResult(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findBy(SelectClause selectClause, ISpecification specificationClause) {
        try {
            return query.selectBy(metaData.getTableName(), selectClause, specificationClause, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findBy(ISpecification specificationClause) {
        try {
            return findBy(null, specificationClause);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T findById(ID object) {
        List<T> queryResult;
        try {
            queryResult = query.selectBy(
                    metaData.getTableName(),
                    new SelectClause("*"),
                    SqlUtils.buildCompareIdClause(metaData, object),
                    clazz
            );
            if (queryResult.isEmpty()) {
                return null;
            } else if (queryResult.size() > 1) {
                throw new RuntimeException("Error: more than one result");
            } else {
                return queryResult.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public T add(T obj) {
        final List<ColumnMetaData> columnMetaData = metaData.getListColumns();

        final List<Object> params = getColumnValList(columnMetaData, obj);

        int result = 0;
        try {
            result = query.insert(metaData, params);
            if (result > 0) {
                return obj;
            } else {
                throw new RuntimeException("Error: insert failed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateById(ID id, T newObj) {
        int result;
        try {
            ISpecification whereClauseId = SqlUtils.buildCompareIdClause(metaData, id);
            SetClause setClause = new SetClause(
                    metaData.getListColumns()
                            .stream()
                            .filter(columnMetaData -> !columnMetaData.isPrimaryKey())
                            .map(columnMetaData -> {
                                try {
                                    return new EqualSpecification(
                                            columnMetaData.getColumnName(),
                                            columnMetaData.getField().get(newObj)
                                    );
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }).toList()
            );

            result = query.update(
                    metaData.getTableName(),
                    setClause,
                    whereClauseId
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(SetClause setClause, ISpecification whereClause)  {
        int result;
        try {
            result = query.update(metaData.getTableName(), setClause, whereClause);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(ID id)  {
        try {
            ISpecification searchSpecification = SqlUtils.buildCompareIdClause(metaData,id);
            return query.delete(metaData.getTableName(), searchSpecification);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean delete(ISpecification whereClause)  {
        try {
            return query.delete(metaData.getTableName(), whereClause);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
