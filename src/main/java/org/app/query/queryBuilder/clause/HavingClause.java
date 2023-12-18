package org.app.query.queryBuilder.clause;

import org.app.query.specification.ISpecification;

public class HavingClause {
    private final ISpecification specification;


    public HavingClause(ISpecification specification) {
        this.specification = specification;
    }

    @Override
    public String toString() {
        return "HAVING " + specification.toString();
    }
}
