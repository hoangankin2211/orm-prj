package org.app.utils;

import org.app.datasource.DataSourceManager;
import org.app.mapper.ObjectMapperManager;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class SqlUtils {

    private static SqlUtils instances = null;

    public static SqlUtils getInstances() {
        if (instances == null) {
            instances = new SqlUtils();
        }
        return instances;
    }

    private SqlUtils() {
    }

    public <T> List<T> handleResultSet(ResultSet resultSet, Class<T> clazz) {
        try {
            final List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                final T obj = clazz.getDeclaredConstructor().newInstance();
                final EntityMetaData entityMetaData = ObjectMapperManager.getInstance().getMapper(clazz);
                final List<ColumnMetaData> columnMetaData = entityMetaData.getColumns();
                for (int i = 0; i < columnMetaData.size(); i++) {

                    final ColumnMetaData column = columnMetaData.get(i);
                    var object = resultSet.getObject(i + 1);
                    column.getField().set(obj, object);

                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> mapSqlTypeToJavaType(int sqlType) {
        return switch (sqlType) {
            case Types.INTEGER, Types.SMALLINT, Types.TINYINT -> Integer.class;
            case Types.BIGINT -> Long.class;
            case Types.FLOAT, Types.REAL -> Float.class;
            case Types.DOUBLE -> Double.class;
            case Types.DECIMAL, Types.NUMERIC -> java.math.BigDecimal.class;
            case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR -> String.class;
            case Types.DATE -> java.sql.Date.class;
            case Types.TIME -> java.sql.Time.class;
            case Types.TIMESTAMP -> java.sql.Timestamp.class;
            case Types.BOOLEAN -> Boolean.class;
            // Add more cases as needed

            default ->
                // Default to Object for unsupported types
                    Object.class;
        };
    }

    public String autoGenerateString() throws SQLException {
        String dataSourceName = DataSourceManager.getInstance().getCurrConnection().getMetaData().getDriverName().toLowerCase();
        if (dataSourceName.contains("postgresql")) {
            return " SERIAL";
        } else if (dataSourceName.contains("mysql")) {
            return " AUTO_INCREMENT";
        } else if (dataSourceName.contains("oracle")) {
            return " AUTO_INCREMENT";
        } else if (dataSourceName.contains("sqlserver")) {
            return " IDENTITY(1,1)";
        } else if (dataSourceName.contains("db2")) {
            return " AUTO_INCREMENT";
        } else if (dataSourceName.contains("sqlite")) {
            return " AUTO_INCREMENT";
        } else {
            throw new SQLException("Error: unsupported auto generate handle for this database type");
        }
    }

    public String getSqlType(Type javaType) {
        if (javaType == int.class || javaType == Integer.class) {
            return "int";
        } else if (javaType == long.class || javaType == Long.class) {
            return "bigint";
        } else if (javaType == short.class || javaType == Short.class) {
            return "smallint";
        } else if (javaType == byte.class || javaType == Byte.class) {
            return "tinyint";
        } else if (javaType == float.class || javaType == Float.class) {
            return "float";
        } else if (javaType == double.class || javaType == Double.class) {
            return "double";
        } else if (javaType == boolean.class || javaType == Boolean.class) {
            return "boolean";
        } else if (javaType == char.class || javaType == Character.class) {
            return "char";
        } else if (javaType == String.class) {
            return "varchar";
        }
        return "varchar";
    }
}
