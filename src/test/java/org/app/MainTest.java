package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.DefaultDataSource;
import org.app.processor.DefaultProcessorImpl;
import org.app.processor.IProcessor;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.junit.jupiter.api.Test;

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

        IProcessor<Employee, Long> defaultProcessor = new DefaultProcessorImpl<>(Employee.class);
        System.out.println(defaultProcessor.findBy(SpecificationClause
                        .builder()
                        .addSpecification(new CompareSpecification("ep_name", "'ormsdfsdfsdf'"))
                )
        );
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
}