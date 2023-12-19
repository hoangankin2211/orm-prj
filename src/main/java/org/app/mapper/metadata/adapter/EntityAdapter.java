package org.app.mapper.metadata.adapter;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.metadata.ForeignKeyMetaData;

import java.lang.reflect.Field;
import java.util.*;

public class EntityAdapter extends EntityMetaData {

    public EntityAdapter(Class<?> clazz) {
        super(null, null, null, null, null, clazz);
        convertToEntityMetadata();
    }

    private void convertToEntityMetadata() {
        Class<?> clazz = this.clazz;

        boolean isExistPrimaryKeyAnnotation = false;
        boolean isEntity = false;

        if (clazz.isAnnotationPresent(Entity.class)) {
            String tableName = clazz.getAnnotation(Entity.class).name();
            if (tableName != null && !tableName.isEmpty()) {
                this.tableName = tableName;
            } else {
                this.tableName = clazz.getSimpleName();
            }
            isEntity = true;
        }

        List<ColumnMetaData> columnMetaDataMap = new ArrayList<>();
        List<ForeignKeyMetaData> foreignKeys = new ArrayList<>();
        Map<String, ColumnMetaData> columnsMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            ColumnMetaData columnMetaData;
            if (f.isAnnotationPresent(ForeignKey.class)) {
                ForeignKeyMetaData foreignKeyMetaData = new ForeignKeyAdapter(f);
                foreignKeys.add(foreignKeyMetaData);
                columnMetaData = foreignKeyMetaData;
            } else {
                 columnMetaData = new ColumnAdapter(f);
                if (columnMetaData.isPrimaryKey()) {
                    isExistPrimaryKeyAnnotation = true;
                    this.primaryKey = columnMetaData;
                }

            }
            columnMetaDataMap.add(columnMetaData);
            columnsMap.put(columnMetaData.getColumnName(), columnMetaData);
        }

        if (isEntity && !isExistPrimaryKeyAnnotation) {
            throw new RuntimeException("Error: Table " + tableName + " must have primary key");
        }

        this.columns = columnMetaDataMap;
        this.columnsMap = columnsMap;
        this.foreignKeys = foreignKeys;
    }


}
