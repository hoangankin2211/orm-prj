package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ColumnMetaData {
    protected boolean isPrimaryKey;
    protected String columnName;
    protected Field field;
    protected boolean nullable = true;
}
