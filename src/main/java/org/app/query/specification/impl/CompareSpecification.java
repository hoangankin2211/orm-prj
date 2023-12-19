package org.app.query.specification.impl;

import org.app.enums.CompareOperation;
import org.app.query.specification.ISpecification;
import org.app.utils.SqlUtils;

public record CompareSpecification(String columns, CompareOperation compareOperation, Object id) implements ISpecification {
    @Override
    public String toString() {
        return columns + compareOperation.getOperation() + SqlUtils.formatSQLParameter(id);
    }

    @Override
    public String createStatement() {
        return toString();
    }
}