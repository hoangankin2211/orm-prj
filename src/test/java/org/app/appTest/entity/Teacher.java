package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
public class Teacher {
    @Id
    private  int id;
    @ForeignKey(referencedTable = Classroom.class, referencedField = "id")
    private int classId;
    @ForeignKey(referencedTable = Course.class, referencedField = "id")
    private  int courseId;

    public Teacher(int id, int classId, int courseList) {
        this.id = id;
        this.classId = classId;
        this.courseId = courseList;
    }
}
