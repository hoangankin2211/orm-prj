package org.app.processor;

import java.util.List;

public interface IProcessor{
    <T> List<T> select(Class<T> clazz) throws Exception;
    <T> int create(T obj) throws Exception;

    <T> int insert(T obj) throws Exception;

    <T> int update(T obj) throws Exception;

    <T> int delete(T obj) throws Exception;

}
