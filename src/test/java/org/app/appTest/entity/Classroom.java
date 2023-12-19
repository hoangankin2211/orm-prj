package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
public class Classroom {
    private  String name;
    @Id
    private  int id;
    @ForeignKey(referencedTable =Student.class, referencedField = "id")
    private  List<Student> studentList;



    public Classroom(String name, int id, List<Student> studentList) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
    }
}
