package org.app.appTest.entity;

import org.app.annotations.Entity;
import org.app.annotations.Id;

@Entity
public class Student {
    private final String name;
    private final int age;
    @Id
    private final int id;
    private final String address;
    private final int gpa;

    public Student(String name, int age, int id, String address, int gpa) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.address = address;
        this.gpa = gpa;
    }
}
