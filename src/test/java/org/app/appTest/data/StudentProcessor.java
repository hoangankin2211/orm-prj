package org.app.appTest.data;

import org.app.appTest.entity.Student;
import org.app.processor.DefaultProcessorImpl;

public class StudentProcessor extends DefaultProcessorImpl<Student,Integer> {
    public StudentProcessor() {
        super(Student.class);
    }
}
