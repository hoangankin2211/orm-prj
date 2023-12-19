package org.app.mapper.metadata;

import lombok.Getter;

@Getter
public class ForeignKeyMetaData extends ColumnMetaData{
    protected Class<?> referencedTable;
    protected String referencedField;
}
