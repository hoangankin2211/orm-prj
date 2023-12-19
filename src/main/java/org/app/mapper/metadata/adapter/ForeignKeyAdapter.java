package org.app.mapper.metadata.adapter;

import org.app.annotations.Column;
import org.app.annotations.ForeignKey;
import org.app.mapper.metadata.ColumnMetaData;
import org.app.mapper.metadata.ForeignKeyMetaData;

import java.lang.reflect.Field;
import java.util.Collection;

public class ForeignKeyAdapter extends ForeignKeyMetaData {
    public ForeignKeyAdapter(Field f) {
        super();
        convertToForeignMetadata(f);
    }

    void convertToForeignMetadata(Field f){
        if (f.getType().isInstance(Collection.class)){
            throw new RuntimeException("Error: foreign key must not be collection");
        }
        this.field = f;

        this.columnName = f.getName().toLowerCase();
        this.isForeignKey = true;
        this.referencedField = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedField();
        this.referencedTable = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedTable();

        ColumnMetaData columnMetaData = new ColumnAdapter(f);
        this.nullable = columnMetaData.isNullable();
        this.resultName = columnMetaData.getResultName();
        this.isPrimaryKey = columnMetaData.isPrimaryKey();
        this.columnName = columnMetaData.getColumnName();
    }
}
