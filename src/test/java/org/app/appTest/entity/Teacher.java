package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
public class Teacher {
    @Id
    private  String id;
    @ForeignKey(referencedTable = Classroom.class, referencedField = "id")
    private int classId;
    @ForeignKey(referencedTable = Course.class, referencedField = "id")
    private  List<Course> courseList;

    public Teacher(String id, int classId, List<Course> courseList) {
        this.id = id;
        this.classId = classId;
        this.courseList = courseList;
    }
}
