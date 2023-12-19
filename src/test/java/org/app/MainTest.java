package org.app;

import org.app.datasource.DataSourceManager;
import org.app.datasource.builder.DataSourceBuilderInfo;
import org.app.enums.CompareOperation;
import org.app.enums.DefaultDataSource;
import org.app.mapper.ObjectMapperManager;
import org.app.mapper.resultset.collection.IResultSetHandler;
import org.app.processor.DefaultProcessorImpl;
import org.app.processor.IProcessor;
import org.app.query.queryBuilder.QueryBuilder;
import org.app.query.queryBuilder.clause.GroupByClause;
import org.app.query.queryBuilder.clause.SelectClause;
import org.app.query.specification.impl.CompareSpecification;
import org.app.query.specification.impl.SpecificationClause;
import org.junit.jupiter.api.Test;

import java.util.List;

class MainTest {
    @Test
    void test() throws Exception {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/orm";
        String username = "postgres";
        String password = "root";
        //        DataSourceManager.getInstance().setDataSource(new H2DataSourceBuilder());
        DataSourceManager.getInstance().setDataSource(
                DefaultDataSource.POSTGRESQL,
                new DataSourceBuilderInfo(jdbcUrl, username, password)
        );
//        getEmployeeLongIProcessor();
        ObjectMapperManager.getInstance().initialize(this.getClass().getPackageName());
    }

//    public static class EmployeeProcessor extends DefaultProcessorImpl<Employee, Long> {
//        private final IResultSetHandler<TestResponse> typeHandler = typeHandlerFactory.getResultSetTypeHandler(TestResponse.class);
//        public EmployeeProcessor() {
//            super(Employee.class);
//        }
//        //Custom Response class
//        List<TestResponse> groupBy() throws Exception {
//            String statement = QueryBuilder
//                    .builder()
//                    .select(new SelectClause("ep_createOn", "name_id"))
//                    .from(metaData.getTableName())
//                    .groupBy(new GroupByClause("ep_createOn", "name_id"))
//                    .having(
//                            SpecificationClause
//                                    .builder()
//                                    .addSpecification(new CompareSpecification("ep_createOn", CompareOperation.EQUALS,"2020-01-01"))
//                                    .build()
//                    )
//                    .build();
//            return typeHandler.getListResult(query.executeQuery(statement));
//        }
//    }


}