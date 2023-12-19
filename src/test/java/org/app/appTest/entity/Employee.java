package org.app.appTest.entity;

import lombok.*;
import org.app.annotations.Column;
import org.app.annotations.Entity;
import org.app.annotations.Id;

import java.sql.Date;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_employee")
public class Employee {
    @Id(value = "ep_id")
    public long id;

    @Column("ep_name")
    public String name;

    @Column("ep_createOn")
    public String createOn;

    @Column("name_id")
    public String nameId;

    public Employee(String name, String createOn, String nameId) {
        this.name = name;
        this.createOn = createOn;
        this.nameId = nameId;
    }
}
