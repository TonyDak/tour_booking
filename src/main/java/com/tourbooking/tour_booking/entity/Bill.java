package com.tourbooking.tour_booking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bill")
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(nullable = false)
    private int pinCode;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String specialRequirement;
    @Column(nullable = false)
    private String others;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private LocalDateTime bookedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;


    @Column(nullable = false)
    private double totalDiscount;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private int totalDiscountPercent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(nullable = false)
    private int totalAdults;

    @Column(nullable = false)
    private int totalChildren;

    @Column(nullable = false)
    private double originalPrice;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double adultPrice;

    @Column(nullable = false)
    private double childPrice;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private Tour tour;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "traveler_id", referencedColumnName = "id", nullable = false)
    private Traveler traveler;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", referencedColumnName = "id", nullable = true)
    private Promotion promotionId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PaymentMethod paymentMethod;

    @Column(nullable = true)
    private String cancelReason;


    public enum BillStatus {
        DRAFT, WAIT, IN_PROGRESS, PAYMENT_FAILED, COMPLETED, CANCELLED
    }

    public enum PaymentMethod {
        CREDIT_CARD, PAYPAL, BANK_TRANSFER, CASH_ON_DELIVERY, OTHER
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (status == null) {
            status = BillStatus.DRAFT;
        }
        if (id == null) {
            id = generateBillId();
        }
        if (pinCode == 0) {
            pinCode = generatePinCode();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateBillId() {
        return "SGU-" + String.format("%09d", new Random().nextInt(1000000000));
    }

    private int generatePinCode() {
        return 1000 + new Random().nextInt(9000);
    }

    public void setPromotion(Promotion promotionId) {
    }

}
