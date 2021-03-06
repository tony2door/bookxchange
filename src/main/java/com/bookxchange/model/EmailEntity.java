package com.bookxchange.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "emails")
@Data
public class EmailEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "sent_date")
    private Date sentDate;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "member_id")
    private Integer memberId;


}
