package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="MOBILEPHONEHISTORY")
@NoArgsConstructor
public class MobilePhoneHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private String mark;

    @Column
    private String model;

    @Column
    private String IMEI;

    @Column
    private String serialNumber;

    @Column
    private String phoneNumber;

    @Column
    private String simNumber;

    @Column
    private String employee;

    @Column
    private String type;



}
