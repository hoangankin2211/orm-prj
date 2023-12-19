package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class EntityMetaData {
    protected String tableName;
    protected Class<?> primaryKeyClass;
    protected Map<String,ColumnMetaData> primaryKey;
    protected Map<String,ForeignKeyMetaData> foreignKeys;
    protected List<ColumnMetaData> columns;
    protected Map<String,ColumnMetaData> columnsMap;
    protected Class<?> clazz;

    public List<ForeignKeyMetaData> getListForeignKey(){
        return foreignKeys.values().stream().toList();
    }

    public List<ColumnMetaData> getPrimaryKeys(){
        return primaryKey.values().stream().toList();
    }
}
