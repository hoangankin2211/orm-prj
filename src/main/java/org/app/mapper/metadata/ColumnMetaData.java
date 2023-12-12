package org.app.mapper.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@AllArgsConstructor
@Getter
public class ColumnMetaData {
    private boolean isPrimaryKey;
    private String columnName;
    private Type type;
    private Field field;


}
