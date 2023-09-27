package com.example.roparmarketplace.modelClasses;

public class my_coupons_Model {

    private String coupon_type;
    private String coupon_date;
    private String userId;
    private String coupon_price;

    public my_coupons_Model(String coupon_type, String coupon_date, String userId, String coupon_price) {
        this.coupon_type = coupon_type;
        this.coupon_date = coupon_date;
        this.userId = userId;
        this.coupon_price = coupon_price;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public String getCoupon_date() {
        return coupon_date;
    }

    public void setCoupon_date(String coupon_date) {
        this.coupon_date = coupon_date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }
}
