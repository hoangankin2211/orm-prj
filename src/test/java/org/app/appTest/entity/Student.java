package org.app.appTest.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.app.annotations.Entity;
import org.app.annotations.Id;

@Entity
@Getter
@NoArgsConstructor
public class Student {
    @Id
    private  Integer id;
    private  String name;
    private  int age;
    private  String address;
    private  int gpa;

    public Student(String name, int age, Integer id, String address, int gpa) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.address = address;
        this.gpa = gpa;
    }
}
