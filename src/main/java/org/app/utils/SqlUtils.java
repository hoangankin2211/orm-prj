package org.app.utils;

import java.lang.reflect.Type;
import java.sql.Types;

public class SqlUtils {

    private static SqlUtils instances = null;

    public static SqlUtils getInstances() {
        if (instances == null){
            instances = new SqlUtils();
        }
        return instances;
    }

    private SqlUtils(){}

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

    public  String getSqlType(Type javaType) {
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
