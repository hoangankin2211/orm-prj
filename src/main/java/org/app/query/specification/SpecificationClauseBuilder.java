package org.app.query.specification;

import org.app.query.specification.impl.SpecificationClause;

import java.util.ArrayList;
import java.util.List;

public interface SpecificationClauseBuilder {

    SpecificationClauseBuilder addSpecification(ISpecification specification);
    SpecificationClauseBuilder addSpecifications( List<ISpecification> specifications);

    SpecificationClause build();
}
