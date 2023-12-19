package org.app.appTest.entity;

import org.app.Student;
import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;

import java.util.List;

@Entity
public class Classroom {
    private  String name;
    private  int id;
    @ForeignKey(referencedTable =Student.class, referencedField = "id")
    private  List<Student> studentList;

    public Classroom(String name, int id, List<Student> studentList) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
    }
}
