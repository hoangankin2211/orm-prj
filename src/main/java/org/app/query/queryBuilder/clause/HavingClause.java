package org.app.query.queryBuilder.clause;

import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.HavingSpecification;

public class HavingClause {
    private final HavingSpecification specification;

    public HavingClause() {
        this.specification = new HavingSpecification();
    }

    public void addSpecification(ISpecification spec) {
        specification.addSpecification(spec);
    }

    @Override
    public String toString() {
        return specification.toString();
    }
}