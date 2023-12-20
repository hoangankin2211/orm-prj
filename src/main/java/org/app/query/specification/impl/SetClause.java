package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetClause implements ISpecification {
    private final List<EqualSpecification>   _specifications;

    public SetClause(EqualSpecification... specifications) {
        _specifications = Arrays.stream(specifications).toList();
    }
    public SetClause(List<EqualSpecification>  specifications) {
        this._specifications = specifications;
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
        return String.join(" , ", buildConditions());
    }
}
