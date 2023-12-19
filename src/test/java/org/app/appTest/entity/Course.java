package org.app.appTest.entity;

import org.app.Student;
import org.app.annotations.Column;
import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;

import java.util.List;
@Entity
public class Course {
    private  String name;
    private  int id;
    @ForeignKey(referencedTable = Student.class, referencedField = "id")
    private  List<Student> studentList;

    @ForeignKey(referencedTable = Teacher.class, referencedField = "id")
    private  Teacher teacher;

    public Course(String name, int id, List<Student> studentList, Teacher teacher) {
        this.name = name;
        this.id = id;
        this.studentList = studentList;
        this.teacher = teacher;
    }
}
