package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.DefaultDataSource;
import org.app.processor.DefaultProcessorImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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


        DefaultProcessorImpl<Employee> defaultProcessor = new DefaultProcessorImpl<>(Employee.class);

//        defaultProcessor.add(new Employee(
//1,
//                "orm2",
//                java.sql.Date.valueOf("2020-01-01").toString(),
//                "1"
//        ));
//
//        defaultProcessor.add(new Employee(
//                2,
//                "Hoang2",
//                java.sql.Date.valueOf("2020-01-01").toString(),
//                "1"
//        ));

        defaultProcessor.delete(new Employee(
                0,
                "Hoang1",
                java.sql.Date.valueOf("2020-01-01").toString(),
                "1"
        ));
        for (Employee employee : defaultProcessor.findAll()) {
            System.out.println(employee.name);
        }
    }
}