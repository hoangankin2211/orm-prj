package org.app.query.specification.composite;

import org.app.query.specification.ISpecification;

import java.util.ArrayList;
import java.util.List;

public class CompositeSpecification<T> implements ISpecification<T> {
    protected final List<ISpecification<T>> specifications;

    public CompositeSpecification() {
        this.specifications = new ArrayList<>();
    }

    public void addSpecification(ISpecification<T> specification) {
        this.specifications.add(specification);
    }

    @Override
    public boolean isSatisfiedBy(T entity) {
        return specifications.stream().allMatch(specification -> specification.isSatisfiedBy(entity));
    }
}
