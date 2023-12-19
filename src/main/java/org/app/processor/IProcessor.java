package org.app.processor;

import org.app.mapper.ObjectMapperManager;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.impl.SpecificationClause;

import java.util.List;

public interface IProcessor<T,ID> {
    ObjectMapperManager mapper = ObjectMapperManager.getInstance();

    List<T> findAll() throws Exception;
    T findById(ID id) throws Exception;
    List<T> findBy(SelectClause selectClause, SpecificationClause specificationClause) throws Exception;
    List<T> findBy(SpecificationClause specificationClause) throws Exception;

    T add(T obj) throws Exception;

    T update(T obj) throws Exception;

    boolean delete(ID id) throws Exception;
}
