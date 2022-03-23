package com.bookxchange.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notifications", schema = "bookOLX")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class NotificationsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "market_book_uuid")
    private String marketBookUuid;
    @Basic
    @Column(name = "email_template_id")
    private Integer templateType;
    @Basic
    @Column(name = "sent")
    private Byte sent;
    @Basic
    @Column(name = "member_uuid")
    private String memberUuid;




    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
