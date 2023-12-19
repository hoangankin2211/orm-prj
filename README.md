# orm-prj

# Simple ORM Framework
The Java ORM Repository offers JDBC-based Object-Relational Mapping, facilitating seamless database integration. With custom annotations and sample frameworks, it simplifies entity mapping and Data Access Management in Java applications.

----
+ ### Lift chestnuts
    + ##### Create a test table
   ``` SQL
   CREATE TABLE `tb_employee`  (
  `ep_id` int(10) AUTO_INCREMENT,
  `ep_name` varchar(20),
  `ep_createOn` datetime,
  PRIMARY KEY (`ep_id`)
  );
    ```
    + ##### ADD A MAVEN REFERENCE
   ``` XML
  <dependency>
      <groupId>org.app</groupId>
      <artifactId>orm-prg</artifactId>
      <version>1.0.0</version>
  </dependency>
    ```
    + ##### Create an entity
   ``` Java
  @Setter
  @Getter
  @Table("tb_employee")
  public class Employee extends BaseEntity {
    @PrimaryKey("ep_id")
    private long id;
    @Column("ep_name")
    private String name;
    @Column("ep_createOn")
    private Date createOn;
    @ResultColumn("name_id")
    private String nameId;
  }
   ```
    + ##### Add a data source
   ``` Java
    // 创建数据源，如：c3p0、druid等
    DataSource dataSource = getDataSource();
    // 给数据源起个名字，方便多数据源切换
    GlobalConfig.addDataSource("mysql", dataSource);
   ```
    + ##### Create a query object
   ``` Java
    SimpleQueryFactory factory = new DefaultSimpleQueryFactory("mysql");
    SimpleQuery simpleQuery = factory.createSimpleQuery();
   ```
    + ##### 插入
   ``` Java
    Employee employee = new Employee();
    // 主键自动忽略
    employee.setId(2L);
    employee.setName("Tom");
    employee.setCreateOn(new Date());
    // 查询字段自动忽略
    employee.setNameId("Tom" + 2L);
    int insertCount = simpleQuery.insert(employee);
   ```
    + ##### 查询
   ``` Java
   // 使用Sql对象需要初始化Comment对象
    Sql sql = new Sql();
    sql.initComment("simpleQuery", "Demo", "main", "查询测试");
    sql.appendSql("SELECT *,CONCAT(ep_name,ep_id) `name_id`  FROM tb_employee");
    List<Employee> employees = simpleQuery.select(Employee.class, sql);
   ```
    + ##### 修改
   ``` Java
    Employee employee2 = employees1.get(0);
    employee2.setName("Jack");
    employee2.setCreateOn(new Date());
    int updateCount = simpleQuery.update(employee2);
   ```
    + ##### 删除
   ``` Java
   int deleteCount = simpleQuery.delete(employee2);
   ```

**  栗子暂且举到这里，欢迎大家入群多多交流。 **
# END