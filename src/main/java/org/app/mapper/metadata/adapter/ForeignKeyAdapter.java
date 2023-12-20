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
        if (f.getType().isInstance(Collection.class) || f.getType().isArray()) {
            throw new RuntimeException("Error: foreign key must not be collection");
        }
        this.referencedField = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedField();
        this.referencedTable = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedTable();

        try {
            Field tableField = this.referencedTable.getDeclaredField(this.referencedField);
            if (tableField.getType() != f.getType()) {
                throw new RuntimeException("Error: foreign key in table must be same type with referenced field in referenced table " +  this.referencedTable.getSimpleName());
            }
        } catch (NoSuchFieldException | RuntimeException e) {
            throw new RuntimeException("Error: referenced field not found " + e.getMessage());
        }

        this.field = f;
        this.columnName = f.getName().toLowerCase();
        this.isForeignKey = true;

        ColumnMetaData columnMetaData = new ColumnAdapter(f);
        this.resultName = columnMetaData.getResultName();
        this.isPrimaryKey = columnMetaData.isPrimaryKey();
        this.columnName = columnMetaData.getColumnName();
        f.setAccessible(true);
    }
}
