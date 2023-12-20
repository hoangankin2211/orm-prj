package org.app.appTest.entity;

import lombok.NoArgsConstructor;
import org.app.annotations.Entity;
import org.app.annotations.ForeignKey;
import org.app.annotations.Id;

import java.util.List;

@Entity
@NoArgsConstructor
public class Teacher {
    @Id
    private  int id;
    public Teacher(int id) {
        this.id = id;
    }
}
