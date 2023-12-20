package org.app.appTest.entity;

import lombok.NoArgsConstructor;
import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
@NoArgsConstructor
public class Classroom {
    private  String name;
    @ForeignKey(referencedTable = Teacher.class, referencedField = "id")
    private int teacherId;
    @Id
    private  int id;
}
