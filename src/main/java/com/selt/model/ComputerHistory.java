package com.selt.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="COMPUTER_HISTORY")
@NoArgsConstructor
public class ComputerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private String employee;

    @Column
    private String officeKey;

    @Column
    private String IPAdress;

    @Column
    private String hostName;



}
