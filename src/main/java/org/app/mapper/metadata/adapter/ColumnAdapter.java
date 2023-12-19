package org.app.mapper.metadata.adapter;

import org.app.annotations.Column;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;
import org.app.annotations.ResultColumn;
import org.app.mapper.metadata.ColumnMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ColumnAdapter extends ColumnMetaData {
    public ColumnAdapter(Field field) {
        convertToColumnMetaData(field);
    }

    private void convertToColumnMetaData(Field field) {
        String columnName = field.getName();
        boolean isPrimaryKey = false;

        if (field.isAnnotationPresent(Id.class)){
//            boolean isAutoGenerate = field.getAnnotation(Id.class).autoGenerate();
//
//            if (isAutoGenerate){
//                if (field.getType() != long.class && field.getType() != Long.class && field.getType() != int.class && field.getType() != Integer.class) {
//                    throw new RuntimeException("Error: primary key must be int or long type");
//                }
////                this.autoIncrement = true;
//            }

            isPrimaryKey = true;
            String name = field.getAnnotation(Id.class).value();
            if (!name.isEmpty()){
                columnName = name;
            }
        }

        if (field.isAnnotationPresent(Column.class)){
            String value = field.getAnnotation(Column.class).value();
            if (!value.isEmpty()){
                columnName = value;
            }
        }

        else if (field.isAnnotationPresent(ResultColumn.class)){
            String value = field.getAnnotation(ResultColumn.class).value();
            if (!value.isEmpty()){
                columnName = value;
            }
        }

        //Set the field accessible if it is private or protected or default
        field.setAccessible(true);

        this.isPrimaryKey = isPrimaryKey;
        this.columnName = columnName.toLowerCase();
        this.field = field;
    }
}
