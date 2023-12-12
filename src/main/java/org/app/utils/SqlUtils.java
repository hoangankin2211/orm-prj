package org.app.utils;

public class SqlUtils {

    private static SqlUtils instances = null;

    public static SqlUtils getInstances() {
        if (instances == null){
            instances = new SqlUtils();
        }
        return instances;
    }

    private SqlUtils(){}

    public  String getSqlType(Class<?> javaType) {
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
