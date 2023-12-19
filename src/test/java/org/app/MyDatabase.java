package org.app;

import org.app.annotations.Database;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;

@Database(
        url = "jdbc:postgresql://localhost:5432/orm",
        username = "postgres",
        password = "root"
)
public class MyDatabase {
    DataSource dataSource;

    void test(){

    }
}
