package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class Configuration {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tonerPercent;

    @Column
    private String email;

    @Column
    private Long time;

    @Column
    private Long serviceCallcounter;

    @Column
    private String folderPath;


}
