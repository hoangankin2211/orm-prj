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
    protected Map<String,ColumnMetaData> primaryKeys;
    protected Map<String,ForeignKeyMetaData> foreignKeys;
    protected Map<String,ColumnMetaData> columnsMap;
    protected Class<?> clazz;
    protected Boolean isCompositeKey;

    public List<ForeignKeyMetaData> getListForeignKey(){
        return foreignKeys.values().stream().toList();
    }

    public List<ColumnMetaData> getListPrimaryKeys(){
        return primaryKeys.values().stream().toList();
    }

    public List<ColumnMetaData> getListColumns(){
        return columnsMap.values().stream().toList();
    }

}
