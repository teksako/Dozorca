package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "COUNTER")
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTER_ID")
    private Long id;

    @Column
    private Long counter;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinTable(
            name="COUNTER_PRINTER",
            joinColumns = {@JoinColumn(name="COUNTER_ID")},
            inverseJoinColumns = {@JoinColumn(name="PRINTER_ID")}
    )

    private Printer printer;

}
