package org.app.mapper.adapter;

import org.app.annotations.Entity;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.EntityMetadata;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityObjectAdapter extends EntityMetadata {

    public EntityObjectAdapter(Class<?> clazz) {
        super(null, null, clazz);
        convertToEntityMetadata();
    }

    private void convertToEntityMetadata() {
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
            columnMetaDataMap.put(f.getName(),new FieldAdapter(f));
        }

        this.columnMetaDataMap = columnMetaDataMap;

    }


}
