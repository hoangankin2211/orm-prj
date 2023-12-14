package org.app.query.specification.composite;

import org.app.query.specification.ISpecification;

public class OrSpecification<T> extends CompositeSpecification<T> {

    public OrSpecification(ISpecification<T> left, ISpecification<T> right) {
        addSpecification(left);
        addSpecification(right);
    }

    @Override
    public boolean isSatisfiedBy(T entity) {
        return specifications.stream().anyMatch(specification -> specification.isSatisfiedBy(entity));
    }
}
