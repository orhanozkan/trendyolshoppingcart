package com.main.java.coupon;

public class RateCoupon extends Coupon {

	private double rate;

	public RateCoupon(double minPurchaseAmount, double rate) {
		super(minPurchaseAmount);
		this.rate = rate;
	}

	@Override
	public double getDiscount(double totalAmount) {
		return totalAmount * (rate * 0.01);
	}

	@Override
	public String toString() {
		return "RateCoupon: minPurchaseAmount: " + String.format("%.2f", super.getMinPurchaseAmount()) + "TL, rate: %"
				+ rate;
	}

}