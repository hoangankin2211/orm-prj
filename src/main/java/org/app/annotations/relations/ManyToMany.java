package org.app.annotations.relations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToMany {
    String referencedTable() ;
    String referencedField() ;
}
