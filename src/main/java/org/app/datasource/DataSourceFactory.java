package org.app.datasource;

import lombok.Getter;
import org.app.datasource.builder.IDataSourceBuilder;
import org.app.datasource.builder.impl.PostgreSqlBuilder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceFactory {
    @Getter
    private static final DataSourceFactory instance = new DataSourceFactory();

    private DataSourceFactory(){}

    DataSource createPostgreSQLDataSource(String url, String username, String password){
        IDataSourceBuilder builder = new PostgreSqlBuilder(url, username, password);
        return builder.buildDataSource();
    }

    public DataSource createMSSQLDataSource(String url, String username, String password) {
        return null;
    }

    public DataSource createMySQLDataSource(String url, String username, String password) {
        return null;
    }

    public DataSource createOracleDataSource(String url, String username, String password) {
        return null;
    }
}
