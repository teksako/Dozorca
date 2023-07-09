package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "MOBILEPHONE")
@NoArgsConstructor
public class MobilePhone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOBILEPHONE_ID")
    private Long id;

    @Column
    private String mark;

    @Column
    private String model;

    @Column
    private String IMEI;

    @Column
    private String IMEI2;

    @Column
    private String MAC;

    @Column
    private String serialNumber;

    @Column
    private String color;

    @Column
    private Boolean demage;

    @Column
    private Boolean hasUser;

    @Column
    private String note;


    @OneToOne
    @JoinTable(
            name=("PHONE_NUMBER"),
            joinColumns = {@JoinColumn(name = "MOBILEPHONE_ID")},
            inverseJoinColumns = {@JoinColumn(name="NUMBER_ID")}
    )

  private PhoneNumber phoneNumber;

    @OneToOne
    @JoinTable(
            name = ("PHONE_USER"),
            joinColumns = {@JoinColumn(name = "MOBILEPHONE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID")}
    )
    private Employee employee;
}



