package org.app;

import org.app.processor.IProcessor;
import org.app.processor.impl.BaseProcessorImpl;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/orm";
        String username = "postgres";
        String password = "root";

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        IProcessor processor = new BaseProcessorImpl(dataSource.getConnection());

        processor.create(new Employee(
                1L,
                "张三",
                java.sql.Date.valueOf(java.time.LocalDate.now()),
                "1"
        ));

    }
}