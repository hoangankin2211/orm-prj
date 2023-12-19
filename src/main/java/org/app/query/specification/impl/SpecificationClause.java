package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;
import org.app.query.specification.SpecificationClauseBuilder;

import java.util.ArrayList;
import java.util.List;

public class SpecificationClause implements ISpecification, SpecificationClauseBuilder {
    public static SpecificationClauseBuilder builder() {
        return new SpecificationClause();
    }
    private SpecificationClause(){}
    @Override
    public SpecificationClauseBuilder addSpecification(ISpecification specification) {
        specifications.add(specification);
        return this;
    }

    @Override
    public SpecificationClause build() {
        return this;
    }

    private List<String> buildConditions() {
        List<String> conditions = new ArrayList<>();
        for (ISpecification specification : specifications) {
            conditions.add(specification.createStatement());
        }
        return conditions;
    }

    @Override
    public String createStatement() {
        return specifications.isEmpty() ? "" : "(" + String.join(" AND ", buildConditions()) + ")";
    }


}
