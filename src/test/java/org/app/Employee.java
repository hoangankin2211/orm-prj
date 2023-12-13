package org.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.app.annotations.Column;
import org.app.annotations.Entity;
import org.app.annotations.Id;

import java.sql.Date;


@Setter
@Getter
@Entity(name = "tb_employee")
public class Employee {
    @Id("ep_id")
    public long id;
    @Column("ep_name")
    public String name;
    @Column("ep_createOn")
    public Date createOn;
    @Column("name_id")
    public String nameId;

    public Employee(String name, Date createOn, String nameId) {
        this.name = name;
        this.createOn = createOn;
        this.nameId = nameId;
    }
}
