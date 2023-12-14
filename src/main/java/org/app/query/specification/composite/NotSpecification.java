package org.app.query.specification.composite;

import org.app.query.specification.ISpecification;

public class NotSpecification<T> extends CompositeSpecification<T> {

    public NotSpecification(ISpecification<T> specification) {
        addSpecification(specification);
    }

    @Override
    public boolean isSatisfiedBy(T entity) {
        return specifications.isEmpty() || !specifications.get(0).isSatisfiedBy(entity);
    }
}