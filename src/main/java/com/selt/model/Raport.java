package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "RAPORT")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Raport {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RAPORT_ID")
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private Long count;

    @Column
    private String printer;

    @Column
    private String toner;

    @Column
    private String department;

    @Column
    private String MPK;

    @Column
    private String inventoryNumber;

    @Column
    private String user;

}
