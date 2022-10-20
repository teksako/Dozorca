package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "PRINTER")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Printer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRINTER_ID")
    private Long id;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column
    private String serialNumber;

    @Column
    private String IPAdress;

    @Column
    private String MACAdress;

    @Column
    private String user;

    @Column
    private String inventoryNumber;


    @Column
    private Boolean collectCounter;

    @Column
    private Long serviceCounter;



    @OneToOne
    @JoinTable(name = "PRINTER_LOCATION",
            joinColumns = @JoinColumn(name = "PRINTER_ID", referencedColumnName = "PRINTER_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEPARTMENT_ID", referencedColumnName = "DEPARTMENT_ID"))

    private Department department;

    @ManyToMany
    @JoinTable(
            name = "PRINTER_TONERTEST",

            joinColumns = {@JoinColumn(name = "PRINTER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TONER_ID")}
    )

    private List<Toner> tonerList;

    @ManyToMany
    @JoinTable(
            name = "PRINTER_OID",
            joinColumns = {@JoinColumn(name = "PRINTER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "OID_ID")}
    )

    private List<OID> oid;


}
