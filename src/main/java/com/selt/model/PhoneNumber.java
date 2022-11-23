package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "NUMBER")
@NoArgsConstructor
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PHONE_ID")
    private Long id;

    @Column
    private String number;

    @Column
    private String SIMNumber;

    @Column
    private String PIN;

    @Column
    private String PUK;
}
