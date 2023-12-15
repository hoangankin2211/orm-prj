package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationClause implements ISpecification {
    private final List<ISpecification> specifications = new ArrayList<>();

    public static SpecificationClause builder() {
        return new SpecificationClause();
    }


    public SpecificationClause addSpecification(ISpecification specification) {
        specifications.add(specification);
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
