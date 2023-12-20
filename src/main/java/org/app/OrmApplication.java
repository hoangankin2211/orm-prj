package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.datasource.builder.IDataSourceBuilder;
import org.app.enums.DefaultDataSource;
import org.app.mapper.ObjectMapperManager;

import javax.sql.DataSource;
import java.sql.SQLException;

public class OrmApplication {
    private static Class<?> _primarySource;
    public static void run(Class<?> primarySource,DefaultDataSource defaultDataSource,DataSourceBuilderInfo dataSourceBuilderInfo){
        _primarySource = primarySource;
        try {
            DataSourceManager.getInstance().setDataSource(
                    DefaultDataSource.POSTGRESQL,
                    dataSourceBuilderInfo
            );
            ObjectMapperManager.getInstance().initialize(_primarySource.getPackageName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Class<?> primarySource, DataSource dataSource){
        _primarySource = primarySource;
        try {
            DataSourceManager.getInstance().setDataSource(dataSource);
            ObjectMapperManager.getInstance().initialize(_primarySource.getPackageName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Class<?> primarySource, IDataSourceBuilder dataSourceBuilder){
        _primarySource = primarySource;
        try {
            DataSourceManager.getInstance().setDataSource(dataSourceBuilder);
            ObjectMapperManager.getInstance().initialize(_primarySource.getPackageName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}