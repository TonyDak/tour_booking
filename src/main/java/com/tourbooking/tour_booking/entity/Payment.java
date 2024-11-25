package com.tourbooking.tour_booking.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import com.tourbooking.tour_booking.entity.Traveler;
import com.tourbooking.tour_booking.entity.Promotion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment {
    private String id;
    private Tour tour;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Traveler> travelers;
    private double totalDiscount;
    private double totalPrice;
    private int totalDiscountPercent;
    private String specialRequirement;
    private String others;
    private String location;

    @NotEmpty(message = "Họ tên không được để trống")
    private String fullName;

    @Null(message = "Trẻ em không cần email người lớn", groups = Adult.class)
    @NotEmpty(message = "Email không được để trống", groups = Adult.class)
    @Email(message = "Email không hợp lệ", groups = Adult.class)
    private String email;

    @Null(message = "Trẻ em không cần số điện thoại", groups = Adult.class)
    @NotEmpty(message = "Số điện thoại không được để trống", groups = Adult.class)
    private String phone;

    @Null(message = "Người lớn không cần giới tính", groups = Child.class)
    @Null(message = "Người lớn không cần ngày sinh", groups = Child.class)
    private String gender;

    @Null(message = "Người lớn không cần ngày sinh", groups = Child.class)
    private LocalDate birthDate;

    private double adultPrice;
    private double childPrice;

    private int totalAdults = 0;
    private int totalChildren = 0;
    private double adultTotalPrice = 0;
    private double childTotalPrice = 0;
    private List<Promotion> promotions;

    public void calculateTotalPrice() {
        totalAdults = 0;
        totalChildren = 0;
        adultTotalPrice = 0;
        childTotalPrice = 0;

        // Tính tổng số người lớn và trẻ em, đồng thời cộng dồn giá
        for (Traveler traveler : travelers) {
            if (traveler.getType() == 1) { // 1: Người lớn
                totalAdults++;
                adultTotalPrice += adultPrice;
            } else if (traveler.getType() == 2) { // 2: Trẻ em
                totalChildren++;
                childTotalPrice += childPrice;
            }
        }

        // Tổng giá trước chiết khấu
        double totalBeforeDiscount = adultTotalPrice + childTotalPrice;

        // Tính tổng chiết khấu từ tất cả các khuyến mãi
        totalDiscount = 0;
        for (Promotion promotion : promotions) {
            totalDiscount += promotion.calculateDiscount(totalBeforeDiscount);
        }

        // Áp dụng chiết khấu cố định và phần trăm (nếu có)
        double discountAmount = totalDiscount + (totalBeforeDiscount * totalDiscountPercent / 100);

        // Đảm bảo tổng giá không âm
        totalPrice = Math.max(0, totalBeforeDiscount - discountAmount);
    }
    public List<Traveler> getAdults() {
        List<Traveler> adults = new ArrayList<>();
        for (Traveler traveler : travelers) {
            if (traveler.getType() == 1) { // 1: Người lớn
                adults.add(traveler);
            }
        }
        return adults;
    }

    // Phương thức để lấy danh sách trẻ em
    public List<Traveler> getChildren() {
        List<Traveler> children = new ArrayList<>();
        for (Traveler traveler : travelers) {
            if (traveler.getType() == 2) { // 2: Trẻ em
                children.add(traveler);
            }
        }
        return children;
    }
    public interface Adult { }
    public interface Child { }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Traveler> getTravelers() {
        return travelers;
    }

    public void setTravelers(List<Traveler> travelers) {
        this.travelers = travelers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalDiscountPercent() {
        return totalDiscountPercent;
    }

    public void setTotalDiscountPercent(int totalDiscountPercent) {
        this.totalDiscountPercent = totalDiscountPercent;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public @NotEmpty(message = "Họ tên không được để trống") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotEmpty(message = "Họ tên không được để trống") String fullName) {
        this.fullName = fullName;
    }

    public @Null(message = "Trẻ em không cần email người lớn", groups = Adult.class) @NotEmpty(message = "Email không được để trống", groups = Adult.class) @Email(message = "Email không hợp lệ", groups = Adult.class) String getEmail() {
        return email;
    }

    public void setEmail(@Null(message = "Trẻ em không cần email người lớn", groups = Adult.class) @NotEmpty(message = "Email không được để trống", groups = Adult.class) @Email(message = "Email không hợp lệ", groups = Adult.class) String email) {
        this.email = email;
    }

    public @Null(message = "Trẻ em không cần số điện thoại", groups = Adult.class) @NotEmpty(message = "Số điện thoại không được để trống", groups = Adult.class) String getPhone() {
        return phone;
    }

    public void setPhone(@Null(message = "Trẻ em không cần số điện thoại", groups = Adult.class) @NotEmpty(message = "Số điện thoại không được để trống", groups = Adult.class) String phone) {
        this.phone = phone;
    }

    public @Null(message = "Người lớn không cần giới tính", groups = Child.class) @Null(message = "Người lớn không cần ngày sinh", groups = Child.class) String getGender() {
        return gender;
    }

    public void setGender(@Null(message = "Người lớn không cần giới tính", groups = Child.class) @Null(message = "Người lớn không cần ngày sinh", groups = Child.class) String gender) {
        this.gender = gender;
    }

    public @Null(message = "Người lớn không cần ngày sinh", groups = Child.class) LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@Null(message = "Người lớn không cần ngày sinh", groups = Child.class) LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public double getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(double childPrice) {
        this.childPrice = childPrice;
    }

    public int getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(int totalChildren) {
        this.totalChildren = totalChildren;
    }

    public int getTotalAdults() {
        return totalAdults;
    }

    public void setTotalAdults(int totalAdults) {
        this.totalAdults = totalAdults;
    }

    public double getAdultTotalPrice() {
        return adultTotalPrice;
    }

    public void setAdultTotalPrice(double adultTotalPrice) {
        this.adultTotalPrice = adultTotalPrice;
    }

    public double getChildTotalPrice() {
        return childTotalPrice;
    }

    public void setChildTotalPrice(double childTotalPrice) {
        this.childTotalPrice = childTotalPrice;
    }
}
