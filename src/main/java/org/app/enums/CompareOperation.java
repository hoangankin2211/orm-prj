package org.app.enums;

public enum CompareOperation {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),

    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<="),
    LIKE("LIKE");

    private final String operation;

    CompareOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
