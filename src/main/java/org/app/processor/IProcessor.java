package org.app.processor;

import org.app.mapper.ObjectMapperManager;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.SetUpdateClause;
import org.app.query.specification.impl.SpecificationClause;

import java.util.List;

public interface IProcessor<T,ID> {
    ObjectMapperManager mapper = ObjectMapperManager.getInstance();

    List<T> findAll() throws Exception;

    List<T> findBy(SelectClause selectClause, ISpecification specificationClause);

    List<T> findBy(ISpecification specificationClause);

    T findById(ID id) throws Exception;

    T add(T obj) throws Exception;

    T updateById(ID id, T obj) throws Exception;

    boolean update(SetUpdateClause setClause, SpecificationClause whereClause) throws Exception;

    boolean delete(ID id) throws Exception;

    boolean delete(ISpecification whereClause);
}
