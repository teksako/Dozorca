package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "TONER")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Toner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TONER_ID")
    private Long id;

    @Column
    private String tonerName;

}
