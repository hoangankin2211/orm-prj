package org.app.utils;

import org.app.annotations.Entity;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ObjectClassUtils {
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation>  annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    //Get all class names that annotated [annotation] in the package
    public static Set<Class<?>> getClassesWithAnnotation(String packageName, Class<? extends Annotation> annotation) {
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
                                if (hasAnnotation(clazz,annotation)) {
                                    classes.add(clazz);
                                }
                            } else {
                                classes.addAll(getClassesWithAnnotation(className,annotation));
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
}
