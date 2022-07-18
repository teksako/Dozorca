package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MAGAZINE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Magazine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long count;


    @OneToOne
    @JoinTable
            (
                    name = "toner_magazine",
                    joinColumns = {@JoinColumn(name = "MAGAZINE_ID")},
                    inverseJoinColumns = {@JoinColumn(name = "TONER_ID")}
            )
    private Toner toner;
}
