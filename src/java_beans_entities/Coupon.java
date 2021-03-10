package java_beans_entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Coupon {
    private int id;
    private int companyId;
    private int categoryID;
    private String title;// "50 אחוז הנחה על טיפולי ספא"
    private String description; // "פירוט יותר מדוייק"
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int amount; // How much coupons like that we want to sell
    private double price;
    private String image;
    CouponStatus couponStatus = CouponStatus.ABLE; // // TODO IN BusinessLogic: only after call boughtCoupon() OR expiredCoupon() method - the clientStatus turns to disable.

    public Coupon(int id, int companyId, int categoryID, String title, String description,
                  LocalDateTime startDate, LocalDateTime endDate, int amount, double price, String image) {
        this.id = id;
        this.companyId = companyId;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(int companyId, int categoryID, String title, String description,
                  LocalDateTime startDate, LocalDateTime endDate, int amount, double price, String image) {
        this.companyId = companyId;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(int id, int companyId, int categoryID, String title, String description, LocalDateTime startDate, LocalDateTime endDate, int amount, double price, String image, CouponStatus couponStatus) {
        this.id = id;
        this.companyId = companyId;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
        this.couponStatus = couponStatus;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
