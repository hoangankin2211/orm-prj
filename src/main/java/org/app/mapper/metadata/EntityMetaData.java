package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityMetaData {
    protected String tableName;
    protected ColumnMetaData primaryKey;
    protected List<ForeignKeyMetaData> foreignKeys;
    protected List<ColumnMetaData> columns;
    protected Map<String,ColumnMetaData> columnsMap;
    protected Class<?> clazz;
}
