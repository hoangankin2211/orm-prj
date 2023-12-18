package org.app.query.specification;

import org.app.query.specification.impl.SpecificationClause;

import java.util.ArrayList;
import java.util.List;

public interface SpecificationClauseBuilder {
    List<ISpecification> specifications = new ArrayList<>();

    SpecificationClauseBuilder addSpecification(ISpecification specification);

    SpecificationClause build();
}
