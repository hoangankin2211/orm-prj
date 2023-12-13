package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityMetaData {
    protected String tableName;
    protected List<ColumnMetaData> columnMetaDataMap;
    protected Class<?> clazz;
}
