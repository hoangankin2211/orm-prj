package org.app.mapper.adapter;

import org.app.annotations.Column;
import org.app.annotations.Id;
import org.app.mapper.metadata.ColumnMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FieldAdapter extends ColumnMetaData {
    public FieldAdapter(Field field) {
        convertToColumnMetaData(field);
    }

    private void convertToColumnMetaData(Field field) {
        String columnName = field.getName();
        Type columnType = field.getType();
        boolean isPrimaryKey = false;

        if (field.isAnnotationPresent(Column.class)){
            columnName = field.getAnnotation(Column.class).value();
        }

        if (field.isAnnotationPresent(Id.class)){
            boolean isAutoGenerate = field.getAnnotation(Id.class).autoGenerate();

            if (isAutoGenerate  ){
                if (field.getType() != long.class && field.getType() != Long.class && field.getType() != int.class && field.getType() != Integer.class) {
                    throw new RuntimeException("Error: primary key must be int or long type");
                }
                this.autoIncrement = true;
            }

            isPrimaryKey = true;
            String name = field.getAnnotation(Id.class).value();
            if (!name.isEmpty()){
                columnName = name;
            }
        }

        this.isPrimaryKey = isPrimaryKey;
        this.columnName = columnName;
        this.type = columnType;
        this.field = field;
    }
}
