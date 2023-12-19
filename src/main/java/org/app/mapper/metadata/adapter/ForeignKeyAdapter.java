package org.app.mapper.metadata.adapter;

import org.app.annotations.Column;
import org.app.annotations.ForeignKey;
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
        this.columnName = field.getAnnotation(ForeignKey.class).name();
        if (columnName.isEmpty()){
            columnName = f.getName();
        }


        this.isPrimaryKey = false;

        this.referencedField = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedField();
        this.referencedTable = f.getAnnotation(org.app.annotations.ForeignKey.class).referencedTable();
    }
}
