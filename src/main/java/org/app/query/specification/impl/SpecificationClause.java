package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;
import org.app.query.specification.SpecificationClauseBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificationClause implements ISpecification, SpecificationClauseBuilder {
    private final List<ISpecification> _specifications = new ArrayList<>();

    private final String delimiter;

    private SpecificationClause(String delimiter) {
        this.delimiter = delimiter;
    }

    public static SpecificationClauseBuilder anyOf(ISpecification ...specifications) {
        SpecificationClause specificationClause = new SpecificationClause(" OR ");
        specificationClause.addSpecifications(Stream.of(specifications).collect(Collectors.toList()));
        return specificationClause;
    }

    public static SpecificationClauseBuilder allOf(ISpecification ...specifications) {
        SpecificationClause specificationClause = new SpecificationClause(" AND ");
        specificationClause.addSpecifications(Stream.of(specifications).collect(Collectors.toList()));
        return specificationClause;
    }


    @Override
    public SpecificationClauseBuilder addSpecification(ISpecification specification) {
        _specifications.add(specification);
        return this;
    }

    public SpecificationClauseBuilder addSpecifications(List<ISpecification> specifications) {
        _specifications.addAll(specifications);
        return this;
    }

    @Override
    public SpecificationClause build() {
        return this;
    }

    private List<String> buildConditions() {
        List<String> conditions = new ArrayList<>();
        for (ISpecification specification : _specifications) {
            conditions.add(specification.createStatement());
        }
        return conditions;
    }

    @Override
    public String createStatement() {
        return !Objects.equals(delimiter, " , ")
                ? _specifications.isEmpty() ? "" : "(" + String.join(delimiter, buildConditions()) + ")" :
                String.join(delimiter, buildConditions());
    }
}
