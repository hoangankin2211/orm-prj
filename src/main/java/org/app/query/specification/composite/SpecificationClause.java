package org.app.query.specification.composite;

import org.app.query.specification.ISpecification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationClause<T> extends CompositeSpecification<T> {
    @Override
    public String toString() {
        return specifications.isEmpty() ? "" : "(" + String.join(" AND ", buildConditions()) + ")";
    }

    private List<String> buildConditions() {
        List<String> conditions = new ArrayList<>();
        for (ISpecification<T> specification : specifications) {
            conditions.add(specification.toString());
        }
        return conditions;
    }
}
