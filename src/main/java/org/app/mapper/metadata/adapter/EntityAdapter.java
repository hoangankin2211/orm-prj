package org.app.mapper.metadata.adapter;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.IdClass;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;
import org.app.mapper.metadata.ForeignKeyMetaData;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EntityAdapter extends EntityMetaData {

    public EntityAdapter(Class<?> clazz) {
        this.clazz = clazz;
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

        if (clazz.isAnnotationPresent(IdClass.class)) {
            this.primaryKeyClass = clazz.getAnnotation(IdClass.class).value();
            this.isCompositeKey = true;
        } else {
            this.isCompositeKey = false;
        }

        Map<String, Field> primaryFieldMap = this.isCompositeKey ?
                Arrays.stream(this.primaryKeyClass.getDeclaredFields())
                        .collect(Collectors.toMap(Field::getName, field -> field)
                        )
                : new HashMap<>();

        Map<String, ColumnMetaData> primaryKeys = new HashMap<>();
        Map<String, ForeignKeyMetaData> foreignKeys = new HashMap<>();
        Map<String, ColumnMetaData> columnsMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            ColumnMetaData columnMetaData;
            if (f.isAnnotationPresent(ForeignKey.class)) {
                columnMetaData = new ForeignKeyAdapter(f);
                foreignKeys.put(columnMetaData.getColumnName(), (ForeignKeyMetaData) columnMetaData);
            } else {
                columnMetaData = new ColumnAdapter(f);
            }

            if (columnMetaData.isPrimaryKey()) {
                isExistPrimaryKeyAnnotation = true;

                Field primaryField = columnMetaData.getField();

                // Checking if column annotated @Id field exists in primary key class
                if (isCompositeKey && (!primaryFieldMap.containsKey(primaryField.getName())
                        || primaryFieldMap.get(primaryField.getName()).getType() != primaryField.getType())
                ) {
                    throw new RuntimeException(
                            "Error:  Primary key class " + this.primaryKeyClass.getName()
                                    + " must have field " + primaryField.getName()
                                    + " with type " + primaryField.getType().getName());
                }

                primaryKeys.put(columnMetaData.getColumnName(), columnMetaData);
            }

            columnsMap.put(columnMetaData.getColumnName(), columnMetaData);
        }

        if (isEntity && !isExistPrimaryKeyAnnotation) {
            throw new RuntimeException("Error: Table " + tableName + " must have primary key");
        }

        this.columnsMap = columnsMap;
        this.foreignKeys = foreignKeys;
        this.primaryKeys = primaryKeys;
    }


}
