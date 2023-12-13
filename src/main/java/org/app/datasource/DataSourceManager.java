package org.app.datasource;

import lombok.Getter;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.datasource.builder.IDataSourceBuilder;
import org.app.enums.DefaultDataSource;
import org.app.query.IQuery;
import org.app.query.impl.DefaultQueryImpl;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceManager {
    private static DataSourceManager instance = null;

    @Getter
    private Connection currConnection = null;

    private DataSource currDataSource = null;

    private DataSourceManager(){
        iQueries = Map.of(
                "default",new DefaultQueryImpl()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (currConnection!=null)
                {
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

    private final Map<String,IQuery> iQueries ;

    public void subscribe(String key,IQuery iQuery){
        this.iQueries.put(key,iQuery);
    }

    public void unSubscribe(String key){
        this.iQueries.remove(key);
    }

    public IQuery getQuery(String key){
        return this.iQueries.get(key);
    }

    void changeDataSource(Connection connection){
        iQueries.values().forEach(iQuery -> iQuery.changeDataSourceConnection(connection));
    }

    public void setDataSource(DataSource dataSource) throws SQLException {
        this.currDataSource = dataSource;
        this.currConnection = dataSource.getConnection();

        changeDataSource(currConnection);

    }

    public void setDataSource(IDataSourceBuilder builder) throws SQLException {
        if (currConnection!=null){
            currConnection.close();
            currConnection = null;
            currDataSource = null;
        }

        this.currDataSource = builder.buildDataSource();
        this.currConnection = this.currDataSource.getConnection();

        changeDataSource(currConnection);
    }

    public void setDataSource(DefaultDataSource dataSourceType, DataSourceBuilderInfo info) throws SQLException {
        if (currConnection!=null){
            currConnection.close();
            currConnection = null;
            currDataSource = null;
        }
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

        changeDataSource(currConnection);
    }

}
