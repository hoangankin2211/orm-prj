package org.app.query.queryBuilder;

import org.app.mapper.metadata.ColumnMetaData;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.utils.SqlUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//Builder
public class QueryBuilder {
    private final StringBuilder query;
    private QueryBuilder() {
        this.query = new StringBuilder();
    }


    public static QueryBuilder builder() {
        return new QueryBuilder();
    };


    private String generateCreateColumnSql(ColumnMetaData columnMetaData) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(columnMetaData.getColumnName());
        sqlBuilder.append(" ");
        sqlBuilder.append(SqlUtils.getInstances().getSqlType(columnMetaData.getField().getType()));
        if (columnMetaData.isPrimaryKey()) {
//            if (columnMetaData.isAutoIncrement()){
//                try {
//                    sqlBuilder.append(SqlUtils.getInstances().autoGenerateString());
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            sqlBuilder.append(" PRIMARY KEY");
        }
        System.out.println("QueryBuilder: " + sqlBuilder.toString());
        return sqlBuilder.toString();
    }


    public QueryBuilder create(String tableName, List<ColumnMetaData> columnMetaData) {
        if (columnMetaData.isEmpty()) {
            throw new RuntimeException("Error: columnMetaData is empty");
        }

        query.append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(
                        columnMetaData
                                .stream()
                                .map(this::generateCreateColumnSql)
                                .collect(Collectors.joining(",", "(", ")"))
                );

        return this;
    }

    public QueryBuilder insert(String tableName, List<ColumnMetaData> columnMetaData) {
        if (columnMetaData.isEmpty()) {
            throw new RuntimeException("Error: columnMetaData is empty");
        }

        query.append("INSERT INTO ")
                .append(tableName)
                .append(
                        columnMetaData
                                .stream()
                                .map(ColumnMetaData::getColumnName)
                                .collect(Collectors.joining(",", "(", ")"))
                )
                .append(" VALUES ")
                .append(
                        Collections
                                .nCopies(columnMetaData.size(), "?")
                                .stream()
                                .collect(Collectors.joining(",", "(", ")"))
                )
                .append(";")
                .append(System.lineSeparator());

        return this;
    }


    public QueryBuilder update(String tableName, List<ColumnMetaData> columnMetaData) {
        query.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(
                        columnMetaData
                                .stream()
                                .filter(column -> !column.isPrimaryKey())
                                .map(ColumnMetaData::getColumnName)
                                .collect(Collectors.joining("=?,", "", "=?"))
                );
        return this;
    }

    public QueryBuilder delete(String tableName) {
        query.append("DELETE FROM ")
                .append(tableName);
        return this;
    }


    public QueryBuilder select(SelectClause selectClause) {
        query.append("SELECT ").append(selectClause);
        return this;
    }

    public QueryBuilder selectAll() {
        query.append("SELECT *");
        return this;
    }

    public QueryBuilder from(String tableName) {
        query.append(" FROM ").append(tableName);
        return this;
    }

    public QueryBuilder where(ISpecification specificationClause) {
        query.append(" WHERE ").append(specificationClause.createStatement());
        return this;
    }

    public QueryBuilder groupBy(GroupByClause groupByClause) {
        query.append(" GROUP BY ").append(groupByClause);
        return this;
    }

    public QueryBuilder having(ISpecification specificationClause) {
        query.append(" HAVING ").append(specificationClause.createStatement());
        return this;
    }

    public String build() {
        return query
                .append(";")
                .append(System.lineSeparator()).toString();
    }
}




