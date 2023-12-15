package org.app.query.queryBuilder.clause;

public class GroupByClause {
    private final String[] columns;

    public GroupByClause(String... columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String column : columns) {
            result.append(column)
                    .append(", ");
        }
        result.delete(result.length() - 2, result.length());  // Remove the last comma and space
        return result.toString();
    }
}
