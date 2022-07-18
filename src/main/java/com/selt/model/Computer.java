package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "COMPUTER")
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class Computer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPUTER_ID")
    private Long id;

    @Column
    private String model;

    @Column
    private String manufacturer;

    @Column
    private String serialNumber;

    @Column
    private String hostname;

    @Column
    private String IPAdress;

    @Column
    private String MACAdress;

    @OneToOne
    @JoinTable(
            name = "computer_windows_license",
            joinColumns = {@JoinColumn(name = "COMPUTER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "WINDOWS_ID")}
    )
    private Windows windowsKey;

    @OneToOne
    @JoinTable(
            name="computer_office_license",
            joinColumns = {@JoinColumn(name="COMPUTER_ID")},
            inverseJoinColumns ={@JoinColumn(name="OFFICE_ID")}
    )
    private Office officeKey;

    @OneToOne
    @JoinTable(
            name="computer_owner",
            joinColumns = {@JoinColumn(name ="COMPUTER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID")}
    )
    private Employee employee;
}
