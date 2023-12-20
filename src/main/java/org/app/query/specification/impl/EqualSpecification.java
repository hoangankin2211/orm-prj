package org.app.query.specification.impl;

import org.app.enums.CompareOperation;

public class EqualSpecification extends CompareSpecification {
    public EqualSpecification(String columns,Object id) {
        super(columns, CompareOperation.EQUALS, id);
    }
}
