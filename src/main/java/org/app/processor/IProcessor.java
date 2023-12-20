package org.app.processor;

import org.app.mapper.ObjectMapperManager;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SetClause;

import java.util.List;

public interface IProcessor<T,ID> {
    ObjectMapperManager mapper = ObjectMapperManager.getInstance();

    List<T> findAll() throws Exception;

    List<T> findBy(SelectClause selectClause, ISpecification specificationClause);

    List<T> findBy(ISpecification specificationClause);

    T findById(ID id) throws Exception;

    T add(T obj) throws Exception;

    void updateById(ID id, T obj) throws Exception;

    void update(SetClause setClause, ISpecification whereClause) throws Exception;

    boolean delete(ID id) throws Exception;

    boolean delete(ISpecification whereClause);
}
