package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;

import java.util.ArrayList;
import java.util.List;

public class HavingSpecification implements ISpecification {
    private final List<ISpecification> specifications;

    public HavingSpecification() {
        this.specifications = new ArrayList<>();
    }

    public void addSpecification(ISpecification specification) {
        this.specifications.add(specification);
    }

    @Override
    public String createStatement() {
        return null;
    }

//    @Override
//    public boolean isSatisfiedBy(T entity) {
//        return specifications.stream().allMatch(specification -> specification.isSatisfiedBy(entity));
//    }
}