package org.app.query.specification.impl;

import org.app.query.specification.ISpecification;

public record CompareSpecification(String columns, Object id) implements ISpecification {
    @Override
    public String toString() {
        return columns + " = " + id;
    }

    @Override
    public String createStatement() {
        return toString();
    }
}