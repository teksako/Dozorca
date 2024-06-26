package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "LAPTOP_HISTORY")
@NoArgsConstructor
public class LaptopHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAPTOP_HISTORY_ID")
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private String inventoryNumber;

    @Column
    private String serialNumber;

    @Column
    private String manufacturer;

    @Column
    private String model;

    @Column
    private String employee;

    @Column
    private String type;

    @Column(unique = true)
    private String protocolName;

    @Column
    private String user;

}
