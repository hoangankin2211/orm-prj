package org.app.mapper;

import org.app.annotations.Entity;
import org.app.datasource.DataSourceManager;
import org.app.mapper.metadata.ForeignKeyMetaData;
import org.app.mapper.metadata.adapter.EntityAdapter;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.executor.IQueryExecutor;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.utils.ObjectClassUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.app.datasource.DataSourceManager.DEFAULT_QUERY;
import static org.app.utils.ObjectClassUtils.getTableName;

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
            mapper = new EntityAdapter(clazz);
            mappers.put(clazz, mapper);
        }

        return mapper;
    }

    void checkingDefaultConstructor(Class<?> clazz){
        try {
            clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize(String packageName) {
        IQueryExecutor query = DataSourceManager.getInstance().getQuery(DEFAULT_QUERY);
        for (Class<?> clazz : ObjectClassUtils.getClassesWithAnnotation(packageName, Entity.class)) {
            checkingDefaultConstructor(clazz);
            EntityMetaData mapper = new EntityAdapter(clazz);
            try {
                query.create(mapper);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mappers.put(clazz, mapper);
        }

        mappers.forEach((aClass, entityMetaData) -> {
            for (ForeignKeyMetaData foreignKey : entityMetaData.getForeignKeys().values().stream().toList()) {
                try {
                    query.execute(QueryBuilder.builder()
                            .alterTable(entityMetaData.getTableName())
                            .addForeignKey(
                                    foreignKey.getColumnName(),
                                    getTableName(foreignKey.getReferencedTable()),
                                    foreignKey.getReferencedField()
                            )
                            .build());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
