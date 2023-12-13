package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.app.mapper.metadata.ColumnMetaData;

import java.util.Map;

@AllArgsConstructor
@Getter
public class EntityMetadata {
    protected String tableName;
    protected Map<String, ColumnMetaData> columnMetaDataMap;
    protected Class<?> clazz;
}
