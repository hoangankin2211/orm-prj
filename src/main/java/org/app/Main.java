package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.DefaultDataSource;
import org.app.processor.IProcessor;
import org.app.processor.impl.BaseProcessorImpl;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/orm";
        String username = "postgres";
        String password = "root";

        DataSourceManager.getInstance().initDefaultDataSource(
                DefaultDataSource.POSTGRESQL,
                new DataSourceBuilderInfo(jdbcUrl,username,password)
        );
    }
}