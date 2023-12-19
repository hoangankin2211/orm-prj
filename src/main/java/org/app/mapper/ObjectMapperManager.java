package org.app.mapper;

import org.app.annotations.Entity;
import org.app.datasource.DataSourceManager;
import org.app.mapper.metadata.ForeignKeyMetaData;
import org.app.mapper.metadata.adapter.EntityAdapter;
import org.app.mapper.metadata.EntityMetaData;
import org.app.query.executor.IQueryExecutor;
import org.app.query.queryBuilder.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectMapperManager {
    private static Map<Class<?>, EntityMetaData> mappers;

    private ObjectMapperManager() {

        mappers = new ConcurrentHashMap<>();

    }

    private static Set<Class<?>> getClassesWithAnnotation(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists()) {
                    String[] files = directory.list();
                    if (files != null) {
                        for (String file : files) {
                            String className = packageName + '.' + file;
                            if (file.endsWith(".class")) {
                                className = className.substring(0, className.length() - 6);
                                Class<?> clazz = Class.forName(className);
                                if (hasAnnotation(clazz)) {
                                    classes.add(clazz);
                                }
                            } else {
                                classes.addAll(getClassesWithAnnotation(className));
                            }

                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static boolean hasAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Entity.class);
    }

    public void initialize(String packageName) {
        IQueryExecutor query = DataSourceManager.getInstance().getQuery("default");
        for (Class<?> clazz : getClassesWithAnnotation(packageName)) {
            EntityMetaData mapper = new EntityAdapter(clazz);
            try {
                query.create(mapper);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            mappers.put(clazz, mapper);
        }

        mappers.forEach((aClass, entityMetaData) -> {
            for (ForeignKeyMetaData foreignKey : entityMetaData.getForeignKeys()) {

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

    public static String getTableName(Class<?> clazz) {
        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity == null) {
            throw new RuntimeException("Error: class " + clazz.getName() + " is not annotated with @Entity");
        }
        if (entity.name().isEmpty()) {
            return clazz.getSimpleName();
        } else {
            return entity.name();
        }
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

}
