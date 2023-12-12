package org.app.mapper.impl;

import org.app.annotations.Column;
import org.app.annotations.Entity;
import org.app.annotations.Id;
import org.app.mapper.metadata.ColumnMetaData;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassAdapter extends EntityMapper {


    public ClassAdapter(Class<?> clazz) {
        super(null, null, clazz);
        buildEntityMapper();
    }

    private void buildEntityMapper() {
        Class<?> clazz  = this.clazz;
        if (clazz.isAnnotationPresent(Entity.class)){
            this.tableName =clazz.getAnnotation(Entity.class).name();
        }

        Map<String,ColumnMetaData> columnMetaDataMap = new HashMap<>();

        Field[] fields = clazz.getDeclaredFields();

//        Map<String, Method> methodMap = Arrays.stream(clazz.getMethods())
//                .collect(Collectors
//                        .toMap(m -> m.getName()
//                                .replace("set", "")
//                                .toLowerCase(), m -> m, (k1, k2) -> k1)
//                );


        for (Field f : fields) {
            String columnName = f.getName();
            Type columnType = f.getType();
            boolean isPrimaryKey = false;

            if (f.isAnnotationPresent(Column.class)){
                columnName = f.getAnnotation(Column.class).value();
            }

            if (f.isAnnotationPresent(Id.class)){
                isPrimaryKey = true;
            }

            ColumnMetaData columnMetaData = new ColumnMetaData(isPrimaryKey,columnName,columnType,f);
            columnMetaDataMap.put(f.getName(),columnMetaData);
        }

        this.columnMetaDataMap = columnMetaDataMap;

    }


}
