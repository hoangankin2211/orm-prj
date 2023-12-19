package org.app.mapper.metadata.adapter;

import org.app.annotations.Entity;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;

import java.lang.reflect.Field;
import java.util.*;

public class EntityAdapter extends EntityMetaData {

    public EntityAdapter(Class<?> clazz) {
        super(null, null, null, null,null, clazz);
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
            }
            else {
                this.tableName = clazz.getName();
            }
            isEntity = true;
        }

        List<ColumnMetaData> columnMetaDataMap = new ArrayList<>();
        Map<String, ColumnMetaData> columnsMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            ColumnMetaData columnMetaData = new ColumnAdapter(f);
            if (columnMetaData.isPrimaryKey()) {
                isExistPrimaryKeyAnnotation = true;
                this.primaryKey = columnMetaData;
            }
            System.out.println(columnMetaData.getColumnName());
            columnMetaDataMap.add(columnMetaData);
            columnsMap.put(columnMetaData.getColumnName(), columnMetaData);
        }

        if (isEntity && !isExistPrimaryKeyAnnotation) {
            throw new RuntimeException("Error: Entity must have primary key");
        }

        this.columns = columnMetaDataMap;
        this.columnsMap = columnsMap;
    }


}
