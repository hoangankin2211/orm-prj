package org.app.mapper.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.app.mapper.metadata.ColumnMetaData;

import java.util.Map;

@AllArgsConstructor
@Getter
public class EntityMapper {
    protected String tableName;
    protected Map<String, ColumnMetaData> columnMetaDataMap;
    protected Class<?> clazz;
}
