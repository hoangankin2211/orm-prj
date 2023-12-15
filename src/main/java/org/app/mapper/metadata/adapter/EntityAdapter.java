package org.app.mapper.metadata.adapter;

import org.app.annotations.Entity;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetaData;

import java.lang.reflect.Field;
import java.util.*;

public class EntityAdapter extends EntityMetaData {

    public EntityAdapter(Class<?> clazz) {
        super(null, null,null, clazz);
        convertToEntityMetadata();
    }

    private void convertToEntityMetadata() {
        Class<?> clazz  = this.clazz;

        if (clazz.isAnnotationPresent(Entity.class)){
            this.tableName =clazz.getAnnotation(Entity.class).name();
        }

        List<ColumnMetaData> columnMetaDataMap = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();

//        Map<String, Method> methodMap = Arrays.stream(clazz.getMethods())
//                .collect(Collectors
//                        .toMap(m -> m.getName()
//                                .replace("set", "")
//                                .toLowerCase(), m -> m, (k1, k2) -> k1)
//                );

        boolean isExistPrimaryKeyAnnotation = false;

        for (Field f : fields) {
            ColumnMetaData columnMetaData = new ColumnAdapter(f);
            if (columnMetaData.isPrimaryKey()){
                isExistPrimaryKeyAnnotation = true;
                this.primaryKey = columnMetaData;
                columnMetaDataMap.add(0,columnMetaData);
            }
            else {
                columnMetaDataMap.add(columnMetaData);
            }
        }

        if (!isExistPrimaryKeyAnnotation){
            throw new RuntimeException("Error: primary key not found");
        }

        this.columns = columnMetaDataMap;
    }


}
