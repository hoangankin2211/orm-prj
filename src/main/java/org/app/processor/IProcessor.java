package org.app.processor;

import org.app.mapper.ObjectMapperManager;

import java.util.List;

public interface IProcessor<T> {
    final ObjectMapperManager mapper = ObjectMapperManager.getInstance();

    List<T> findAll() throws Exception;

    T add(T obj) throws Exception;

    T update(T obj) throws Exception;

    T delete(T obj) throws Exception;
}
