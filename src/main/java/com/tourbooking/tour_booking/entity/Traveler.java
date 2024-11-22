package com.tourbooking.tour_booking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Traveler {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String email;
    private String phoneNumber;
    private String name;
    private LocalDate dateOfBirth;
    private String gender;

    @Column(nullable = false)
    private int type; // 1 = Người lớn, 2 = Trẻ em

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "bill_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Bill bill;

    @PrePersist
    protected void prePersist() {
        if (type == 1) {
            if (email == null || name == null || phoneNumber == null)  {
                throw new IllegalArgumentException("Người lớn cần có email và tên.");
            }
        } else if (type == 2) {
            if (name == null ||dateOfBirth == null || gender == null) {
                throw new IllegalArgumentException("Trẻ em cần có ngày sinh và giới tính.");
            }
        } else {
            throw new IllegalArgumentException("Type không hợp lệ. Vui lòng nhập 1 (người lớn) hoặc 2 (trẻ em).");
        }
    }


}
