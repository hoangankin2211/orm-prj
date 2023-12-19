package org.app;

import org.app.datasource.builder.IDataSourceBuilder;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DataSourceBuilder implements IDataSourceBuilder {

    @Override
    public DataSource buildDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:~/test");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}