package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="WINDOWS_KEY")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Windows {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="WINDOWS_KEY_ID")
    private Long id;

    @Column
    private String windowsVersion;

    @Column
    private String windowsKey;

//    @OneToOne(mappedBy = "laptop")
//    private Set<Laptop> laptops;

}
