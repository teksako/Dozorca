package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(unique = true)
    private String login;

    @Column
    private String fullname;

    @Column
    private String password;

    @Column
    private Boolean enabled;

    @Column
    @Temporal(TemporalType.DATE)
    private Date createDate;



    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name="USER_ID")},
            inverseJoinColumns =  {@JoinColumn(name="ROLE_ID")}
    )
    //@OrderBy("name ASC")
    private List<UserRole> roles;
    //private UserRole role;



}
