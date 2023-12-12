package org.app.mapper.metadata;

import java.util.Map;

public  class EntityMetaData {
    private final Class<?> clazz;

    private String tableName;
    private Map<String, ColumnMetaData> columnMetaDataMap;

    public EntityMetaData(Class<?> clazz) {
        this.clazz = clazz;
    }


    public Class<?> getClazz() {
        return clazz;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public Map<String, ColumnMetaData> getColumnMetaDataMap() {
        return columnMetaDataMap;
    }

    public void setFieldInfoMap(final Map<String, ColumnMetaData> columnMetaDataMap) {
        this.columnMetaDataMap = columnMetaDataMap;
    }
}
