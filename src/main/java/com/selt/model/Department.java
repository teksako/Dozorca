package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "DEPARTMENT")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private Long id;

    @Column
    private String nameOfDepartment;

    @Column
    private Long MPK;

    @ManyToMany
    @JoinTable
            (
            name = "department_location",
            joinColumns = {@JoinColumn(name = "DEPARTMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "LOCATION_ID")}
    )
    private List<Location> locations;


    @OneToOne(orphanRemoval = true)
    @JoinTable(name = "department_printer",
            joinColumns = @JoinColumn(name = "department_department_id"),
            inverseJoinColumns = @JoinColumn(name = "printer_printer_id"))
    private Printer printer;

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
