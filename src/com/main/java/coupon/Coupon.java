package com.main.java.coupon;

public abstract class Coupon implements ICoupon {
	private double minPurchaseAmount;

	public double getMinPurchaseAmount() {
		return minPurchaseAmount;
	}

	public void setMinPurchaseAmount(double minPurchaseAmount) {
		this.minPurchaseAmount = minPurchaseAmount;
	}

	public Coupon(double minPurchaseAmount) {
		super();
		this.minPurchaseAmount = minPurchaseAmount;
	}

	@Override
	public boolean isApplicable(double amount) {
		return amount >= minPurchaseAmount;
	}
}
