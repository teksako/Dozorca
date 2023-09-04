package com.selt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Temp {

  private Long id;
  private Long radio;
  private String start;
  private String end;
  private String tempString;
  private String tempString1;
  private String tempString2;
  private String tempString3;
  private String tempString4;
  private Long id_1;
  private Long id_2;
  private String date;
  private String notice;


  public void setId(Long id) {
    this.id = id;
  }

  @Id
  public Long getId() {
    return id;
  }
}
