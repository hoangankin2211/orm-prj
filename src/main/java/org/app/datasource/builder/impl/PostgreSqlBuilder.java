package org.app.datasource.builder.impl;

import lombok.Setter;
import org.app.annotations.Database;
import org.app.datasource.builder.IDataSourceBuilder;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

@Setter
public class PostgreSqlBuilder implements IDataSourceBuilder {
    private final String url;
    private  final String username;
    private  final String  password;

    public PostgreSqlBuilder(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public PostgreSqlBuilder() {
        Database database = PostgreSqlBuilder.class.getAnnotation(Database.class);
        this.url = database.url();
        this.username = database.username();
        this.password = database.password();
    }

    @Override
    public DataSource buildDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
