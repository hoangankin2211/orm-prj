package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.Id;
import org.app.annotations.IdClass;

@Entity
@IdClass(value = CourseStudentID.class)
public class CourseStudent {
    @Id
    int studentId;
    @Id
    int courseId;
}
