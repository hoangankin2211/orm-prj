package org.app.mapper.resultset;

import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.mapper.resultset.collection.ResultSetHandler;
import org.app.mapper.resultset.primitive.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerFactory {
    private static final Map<Class<?>, ITypeHandler<?>> typeHandlerCache = new ConcurrentHashMap<>();

    private static TypeHandlerFactory instance = null;

    public static TypeHandlerFactory getInstance() {
        if (instance == null) {
            instance = new TypeHandlerFactory();
        }
        return instance;
    }

    public TypeHandlerFactory() {
        registerDefaultTypeHandler();
    }

    private void registerDefaultTypeHandler() {
//        // base
        registerTypeHandler(byte.class, new ByteTypeHandler());
        registerTypeHandler(Byte.class, new ByteTypeHandler());
        registerTypeHandler(short.class, new ShortTypeHandler());
        registerTypeHandler(Short.class, new ShortTypeHandler());
        registerTypeHandler(int.class, new IntegerTypeHandler());
        registerTypeHandler(Integer.class, new IntegerTypeHandler());
        registerTypeHandler(long.class, new LongTypeHandler());
        registerTypeHandler(Long.class, new LongTypeHandler());
        registerTypeHandler(float.class, new FloatTypeHandler());
        registerTypeHandler(Float.class, new FloatTypeHandler());
        registerTypeHandler(double.class, new DoubleTypeHandler());
        registerTypeHandler(Double.class, new DoubleTypeHandler());
        registerTypeHandler(boolean.class, new BooleanTypeHandler());
        registerTypeHandler(Boolean.class, new BooleanTypeHandler());

        // Object
        registerTypeHandler(String.class, new StringTypeHandler());
        registerTypeHandler(Date.class, new DateTypeHandler());
    }

    private void registerDefaultTypeHandler(Class<?> clazz, ITypeHandler<?> typeHandler) {
        registerTypeHandler(clazz, typeHandler);
    }

    public void registerTypeHandler(Class<?> key, ITypeHandler<?> typeHandler) {
        typeHandlerCache.put(key, typeHandler);
    }

    public ITypeHandler<?> getTypeHandler(Class<?> clazz) {
        return typeHandlerCache.get(clazz);
    }

    public <T> IResultSetHandler<T> getResultSetTypeHandler(Class<T> clazz) {
        ITypeHandler<?> typeHandler = typeHandlerCache.get(clazz);
        if (typeHandler == null) {
            var resultSetHandler = new ResultSetHandler<>(clazz);
            registerTypeHandler(clazz,resultSetHandler);
            return resultSetHandler;
        }
        if (typeHandler instanceof IResultSetHandler) {
            return (IResultSetHandler<T>) typeHandler;
        }
        throw new RuntimeException("Can not found result set handler for class " + clazz.getName());
    }
}
