package org.app.appTest;

import org.app.OrmApplication;
import org.app.appTest.data.StudentProcessor;
import org.app.appTest.entity.Student;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.CompareOperation;
import org.app.enums.DefaultDataSource;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.EqualSpecification;
import org.app.query.specification.impl.SetUpdateClause;
import org.app.query.specification.impl.SpecificationClause;

//Demo app student management
public class App {
    static StudentProcessor studentProcessor = new StudentProcessor();

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/orm";
        String username = "postgres";
        String password = "root";

        OrmApplication.run(App.class, DefaultDataSource.POSTGRESQL, new DataSourceBuilderInfo(jdbcUrl, username, password));

        testSelectStudent3();
    }

    static void testAddStudent() {
        Student student = new Student("Nguyen Van A", 20, 1, "Ha Noi", 3);
        studentProcessor.add(student);
        Student student2 = new Student("Nguyen Van B", 21, 2, "Ha Noi", 3);
        studentProcessor.add(student2);
        Student student3 = new Student("Nguyen Van C", 22, 3, "Ha Noi", 3);
        studentProcessor.add(student3);
        Student student4 = new Student("Nguyen Van D", 23, 4, "Ha Noi", 3);
        studentProcessor.add(student4);
        Student student5 = new Student("Nguyen Van E", 24, 5, "Ha Noi", 3);
        studentProcessor.add(student5);

        Student student6 = new Student("Nguyen Van F", 25, 6, "Ha Noi", 3);
        studentProcessor.add(student6);
        Student student7 = new Student("Nguyen Van G", 26, 7, "Ha Noi", 3);
        studentProcessor.add(student7);

        Student student8 = new Student("Nguyen Van H", 27, 9, "Ha Noi", 3);
        studentProcessor.add(student8);
    }

    static void displayStudent(Student student) {
        if (student == null){
            System.out.println("Student not found");
            return;
        }
        System.out.println(student.getId() + " " + student.getName() + " " + student.getAge() + " " + student.getAddress() + " " + student.getGpa());
    }

    static void testGetStudent() {
        for (Student student : studentProcessor.findAll()) {
            displayStudent(student);
        }
    }

    static void testUpdateStudent1() {
        studentProcessor.updateById(
                1,
                new Student("Nguyen Van A", 50, 1, "Ha Noi", 3)
        );

        for (Student student : studentProcessor.findAll()) {
            displayStudent(student);
        }
    }

    static void testUpdateStudent2() {
        studentProcessor.update(new SetUpdateClause(
                        new EqualSpecification("name", "Hoang Truong")
                ), SpecificationClause.allOf(
                        new EqualSpecification("name", "Nguyen Van A")
                ).build()
        );
    }

    static void testDeleteStudent() {
        studentProcessor.delete(new CompareSpecification(
                "id",
                CompareOperation.GREATER_THAN,
                7
        ));
    }


    static void testSelectStudent1() {
        for (Student student : studentProcessor.findBy(
                new CompareSpecification(
                        "id",
                        CompareOperation.LESS_THAN_OR_EQUALS,
                        5
                )
        )) {
            displayStudent(student);
        }
    }

    static void testSelectStudent2() {
        for (Student student : studentProcessor.findBy(
                new SelectClause("id", "name"),
                new CompareSpecification(
                        "id",
                        CompareOperation.LESS_THAN_OR_EQUALS,
                        5
                )
        )) {
            displayStudent(student);
        }
    }

    static void testSelectStudent3() {
      displayStudent(studentProcessor.findById(1));
    }
}
