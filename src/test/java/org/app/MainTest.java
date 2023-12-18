package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.DefaultDataSource;
import org.app.mapper.resultset.ITypeHandler;
import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.mapper.resultset.collection.ResultSetHandler;
import org.app.processor.DefaultProcessorImpl;
import org.app.processor.IProcessor;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.ISpecification;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.junit.jupiter.api.Test;

import java.util.List;

class MainTest {
    @Test
    void test() throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/orm";
        String username = "postgres";
        String password = "root";
        //        DataSourceManager.getInstance().setDataSource(new H2DataSourceBuilder());
        DataSourceManager.getInstance().setDataSource(
                DefaultDataSource.POSTGRESQL,
                new DataSourceBuilderInfo(jdbcUrl, username, password)
        );
//        getEmployeeLongIProcessor();

        EmployeeProcessor defaultProcessor = new EmployeeProcessor();
        defaultProcessor.groupBy().forEach(employee -> {
            System.out.println(employee.name);
        });
    }

    private static IProcessor<Employee, Long> getEmployeeLongIProcessor() throws Exception {
        IProcessor<Employee, Long> defaultProcessor = new DefaultProcessorImpl<>(Employee.class);

        defaultProcessor.add(new Employee(
                1,
                "orm2",
                java.sql.Date.valueOf("2020-01-01").toString(),
                "1"
        ));

        defaultProcessor.add(new Employee(
                2,
                "Hoang2",
                java.sql.Date.valueOf("2020-01-01").toString(),
                "1"
        ));

//        defaultProcessor.delete(0L);
        return defaultProcessor;
    }

    public static class EmployeeProcessor extends DefaultProcessorImpl<Employee, Long> {
        public EmployeeProcessor() {
            super(Employee.class);
        }

        List<Employee> groupBy() throws Exception {
            String statement = QueryBuilder
                    .builder()
                    .select(new SelectClause("ep_createOn", "name_id"))
                    .from(metaData.getTableName())
//                    .groupBy(new GroupByClause("ep_createOn", "name_id"))
//                    .having(
//                            SpecificationClause
//                                    .builder()
//                                    .addSpecification(() -> "ep_createOn = '2020-01-01'")
//                                    .build()
//                    )
                    .build();
            return query.executeSql(statement, resultSet -> {
                try {
                    IResultSetHandler<Employee> handler = new ResultSetHandler<>(Employee.class);
                    return handler.getListResult(resultSet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
        }

    }

}