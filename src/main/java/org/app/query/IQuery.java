package org.app.query;

import org.app.mapper.metadata.EntityMetaData;

import java.sql.Connection;
import java.util.List;

public interface IQuery {

    void changeDataSourceConnection(Connection connection);


    <T> List<T> select(String tableName,Class<T> clazz) throws Exception;

    <T> boolean create(EntityMetaData entityMetaData) throws Exception;


    <T> int insert(EntityMetaData entityMetaData,List<Object> params) throws Exception;

    <T> int update(T obj) throws Exception;

    <T> int delete(T obj) throws Exception;

}
