package com.main.java.coupon;

public interface ICoupon {

    boolean isApplicable(double cost);

    double getDiscount(double totalAmount);
}