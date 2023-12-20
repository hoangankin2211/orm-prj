package org.app.query.specification.impl;

import org.app.enums.CompareOperation;
import org.app.query.specification.ISpecification;
import org.app.utils.SqlUtils;

public class CompareSpecification implements ISpecification {
    private final String columns;
    private final CompareOperation compareOperation;
    private final Object id;

    public CompareSpecification(String columns, CompareOperation compareOperation, Object id) {
        this.columns = columns;
        this.compareOperation = compareOperation;
        this.id = id;
    }
    @Override
    public String createStatement() {
         return columns + compareOperation.getOperation() + SqlUtils.formatSQLParameter(id);
    }
}