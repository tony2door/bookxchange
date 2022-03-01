package com.bookxchange.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "transaction", schema = "bookOLX")
@Data
public class TransactionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "market_book_id")
    private String marketBookId;
    @Basic
    @Column(name = "member_id_from")
    private String memberIdFrom;
    @Basic
    @Column(name = "member_id_to")
    private String memberIdTo;
    @Basic
    @Column(name = "transaction_type")
    private String transactionType;
    @Basic
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Basic
    @Column(name = "expected_return_date")
    private Date expectedReturnDate;

}
