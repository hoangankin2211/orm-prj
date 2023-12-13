package org.app.datasource;

import lombok.Getter;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.DefaultDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceManager {
    @Getter
    private static final DataSourceManager instance = new DataSourceManager();

    @Getter
    private Connection currConnection = null;

    @Getter
    private DataSource currDataSource = null;

    private DataSourceManager(){}


    public void setCurrDataSource(DataSource dataSource) throws SQLException {
        this.currDataSource = dataSource;
        this.currConnection = dataSource.getConnection();
    }

    public void initDefaultDataSource(DefaultDataSource dataSourceType, DataSourceBuilderInfo info) throws SQLException {
        switch (dataSourceType)
        {
            case MSSQL:
                this.currDataSource = DataSourceFactory
                        .getInstance()
                        .createMSSQLDataSource(info.getUrl(), info.getUsername(), info.getPassword());
                break;
            case MYSQL:
                this.currDataSource = DataSourceFactory.getInstance().createMySQLDataSource(info.getUrl(), info.getUsername(), info.getPassword());
                break;
            case POSTGRESQL:
                this.currDataSource = DataSourceFactory.getInstance().createPostgreSQLDataSource(info.getUrl(), info.getUsername(), info.getPassword());
                break;
            case ORACLE:
                this.currDataSource = DataSourceFactory.getInstance().createOracleDataSource(info.getUrl(), info.getUsername(), info.getPassword());
                break;
            default:
                throw new SQLException("Unsupported DataSource Type");
        }

        this.currConnection = this.currDataSource.getConnection();
    }

}
