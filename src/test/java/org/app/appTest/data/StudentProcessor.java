package org.app.appTest.data;

import lombok.Getter;
import org.app.appTest.entity.Student;
import org.app.enums.CompareOperation;
import org.app.processor.DefaultProcessorImpl;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.impl.CompareSpecification;

import java.util.List;

public class StudentProcessor extends DefaultProcessorImpl<Student, Integer> {
    public StudentProcessor() {
        super(Student.class);
    }

    public List<TestResponse> findByNameGroupByName() {
        final var typeHandle = typeHandlerFactory.getResultSetTypeHandler(TestResponse.class);
        return typeHandle.getListResult(query.executeQuery(QueryBuilder
                        .builder()
                        .select(new SelectClause("name"))
                        .from("student")
                        .groupBy(new GroupByClause("name"))
                        .having(new CompareSpecification("count(name)", CompareOperation.EQUALS, 1))
                        .build()
                )
        );
    }

    @Getter
    public static class TestResponse {
        private String name;
    }
}