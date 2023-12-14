package org.app.mapper;

import org.app.mapper.adapter.EntityObjectAdapter;
import org.app.mapper.metadata.EntityMetaData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectMapperManager {
    private static Map<Class<?>, EntityMetaData> mappers;

    private ObjectMapperManager() {
        mappers = new ConcurrentHashMap<>();
    }

    private static final class InstanceHolder {
        private static final ObjectMapperManager instance = new ObjectMapperManager();
    }

    public static ObjectMapperManager getInstance() {
        return InstanceHolder.instance;
    }
    public EntityMetaData getMapper(Class<?> clazz) {
        EntityMetaData mapper = mappers.get(clazz);

        if (mapper == null) {
            mapper = new EntityObjectAdapter(clazz);
            mappers.put(clazz, mapper);
        }

        return mapper;
    }
    
}
