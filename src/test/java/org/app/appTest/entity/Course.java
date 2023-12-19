package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
public class Course {
    private String name;
    @Id
    private int id;
    @ForeignKey(referencedTable = Teacher.class, referencedField = "id")
    private int teacherId;

    public Course(String name, int id, int studentId, int teacherId) {
        this.name = name;
        this.id = id;
        this.teacherId = teacherId;

    }
}
