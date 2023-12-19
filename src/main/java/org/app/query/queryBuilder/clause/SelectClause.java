package org.app.query.queryBuilder.clause;

public class SelectClause {
    private final String[] columns;

    public SelectClause(String... columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String column : columns) {
            result.append(column).append(", ");
        }
        result.delete(result.length() - 2, result.length());  // Remove the last comma and space
        return result.toString();
    }
}
