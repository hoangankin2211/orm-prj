package org.app.query.specification.composite;

import org.app.query.specification.ISpecification;

public class AndSpecification<T> extends CompositeSpecification<T> {

    public AndSpecification(ISpecification<T> left, ISpecification<T> right) {
        addSpecification(left);
        addSpecification(right);
    }

}