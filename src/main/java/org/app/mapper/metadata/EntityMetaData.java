package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityMetaData {
    protected String tableName;
    protected ColumnMetaData primaryKey;
    protected List<ColumnMetaData> columns;
    protected Class<?> clazz;
}
