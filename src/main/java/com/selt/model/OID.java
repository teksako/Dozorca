package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "OID")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OID {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OID_ID")
    private Long id;

    @Column
    private String oidName;

    @Column
    private String oidValue;

    @Column
    private String oidProducent;
}
