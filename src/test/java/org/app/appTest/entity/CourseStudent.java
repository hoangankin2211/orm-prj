package org.app.appTest.entity;

import lombok.NoArgsConstructor;
import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;
import org.app.annotations.IdClass;

@Entity
@IdClass(value = CourseStudentID.class)
@NoArgsConstructor
public class CourseStudent {
    @Id
    @ForeignKey(referencedTable = Student.class, referencedField = "id")
    Integer studentId;
    @Id
    @ForeignKey(referencedTable = Course.class, referencedField = "id")
    int courseId;
}
