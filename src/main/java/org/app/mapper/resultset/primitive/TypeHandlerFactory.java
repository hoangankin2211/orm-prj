package org.app.mapper.resultset.primitive;

import org.app.mapper.resultset.ITypeHandler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeHandlerFactory {
    private static final Map<Class<?>, ITypeHandler<?>> typeHandlerCache = new ConcurrentHashMap<>();

    private static TypeHandlerFactory instance = null;

    public static TypeHandlerFactory getInstance() {
        if (instance == null){
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

    public void registerTypeHandler(Class<?> clazz, ITypeHandler<?> typeHandler) {
        typeHandlerCache.put(clazz, typeHandler);
    }
    public ITypeHandler<?>  getTypeHandler(Class<?> clazz) {
        return typeHandlerCache.get(clazz);
    }
}
