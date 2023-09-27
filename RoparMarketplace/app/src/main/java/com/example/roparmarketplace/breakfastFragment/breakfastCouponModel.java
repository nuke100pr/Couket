package com.example.roparmarketplace.breakfastFragment;

public class breakfastCouponModel {

    private String coupon_Type;
    private String coupon_date;
    private String coupon_price;
    private String seller_name;
    private Boolean call;
    private String seller_phone;
    private String seller_id;

    public breakfastCouponModel(String coupon_Type, String coupon_date, String coupon_price, String seller_name,Boolean call,String seller_phone,String seller_id) {
        this.coupon_Type = coupon_Type;
        this.coupon_date = coupon_date;
        this.coupon_price = coupon_price;
        this.seller_name = seller_name;
        this.call = call;
        this.seller_phone = seller_phone;
        this.seller_id=seller_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public Boolean getCall() {
        return call;
    }

    public void setCall(Boolean call) {
        this.call = call;
    }

    public String getCoupon_Type() {
        return coupon_Type;
    }

    public void setCoupon_Type(String coupon_Type) {
        this.coupon_Type = coupon_Type;
    }

    public String getCoupon_date() {
        return coupon_date;
    }

    public void setCoupon_date(String coupon_date) {
        this.coupon_date = coupon_date;
    }

    public String getCoupon_price() {
        return coupon_price;
    }

    public void setCoupon_price(String coupon_price) {
        this.coupon_price = coupon_price;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
}
