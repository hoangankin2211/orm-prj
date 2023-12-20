package org.app.datasource;

import lombok.Getter;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.datasource.builder.IDataSourceBuilder;
import org.app.enums.DefaultDataSource;
import org.app.query.executor.DefaultQueryExecutorImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.app.query.executor.IQueryExecutor;

public class DataSourceManager {
    private static DataSourceManager instance = null;
    public static final String DEFAULT_QUERY= "default";

    private Connection currConnection = null;

    private DataSource currDataSource = null;

    private DataSourceManager() {
        iQueries.put(DEFAULT_QUERY, new DefaultQueryExecutorImpl());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (currConnection != null) {
                    currConnection.close();
                    System.out.println("Shutting down application. Closing database connection.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static DataSourceManager getInstance() {
        if (instance == null) {
            instance = new DataSourceManager();
        }
        return instance;
    }

    private final Map<String, IQueryExecutor> iQueries = new HashMap<>();

    public void subscribe(String key, IQueryExecutor iQueryExecutor) {
        this.iQueries.putIfAbsent(key, iQueryExecutor);
    }

    public void unSubscribe(String key) {
        this.iQueries.remove(key);
    }

    public IQueryExecutor getQuery(String key) {
        return this.iQueries.get(key);
    }

    void changeDataSource(Connection connection) {
        iQueries.values().forEach(iQueryExecutor -> iQueryExecutor.changeDataSourceConnection(connection));
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.currDataSource = dataSource;
        this.currConnection = dataSource.getConnection();
        changeDataSource(currConnection);
    }

    public void setDataSource(IDataSourceBuilder builder) throws SQLException {
        if (currConnection != null) {
            currConnection.close();
            currConnection = null;
            currDataSource = null;
        }

        this.currDataSource = builder.buildDataSource();
        this.currConnection = this.currDataSource.getConnection();

        changeDataSource(currConnection);
    }

    public void setDataSource(DefaultDataSource dataSourceType, DataSourceBuilderInfo info) throws SQLException {
        if (currConnection != null) {
            currConnection.close();
            currConnection = null;
            currDataSource = null;
        }
        switch (dataSourceType) {
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

        try {
            this.currConnection = this.currDataSource.getConnection();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        changeDataSource(currConnection);
    }

}
