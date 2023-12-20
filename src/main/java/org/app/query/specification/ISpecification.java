package org.app.query.specification;

import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;

import java.util.Arrays;

public interface ISpecification {
    String createStatement();

    default ISpecification and(ISpecification specification) {
        return SpecificationClause.allOf().addSpecification(this).addSpecification(specification).build();
    }

    default SpecificationClause or(ISpecification specification) {
        return SpecificationClause.anyOf().addSpecification(this).addSpecification(specification).build();
    }


    default SpecificationClause and(ISpecification... specifications) {
        return SpecificationClause.allOf().addSpecifications(Arrays.asList(specifications)).build();
    }

    default SpecificationClause or(ISpecification... specifications) {
        return SpecificationClause.anyOf().addSpecifications(Arrays.asList(specifications)).build();
    }

}
