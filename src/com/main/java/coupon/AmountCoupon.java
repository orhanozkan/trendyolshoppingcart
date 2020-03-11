package com.main.java.coupon;

public class AmountCoupon extends Coupon {

	private double amount;

	public AmountCoupon(double minPurchaseAmount, double amount) {
		super(minPurchaseAmount);
		this.amount = amount;
	}

	@Override
	public double getDiscount(double totalAmount) {
		return amount;
	}

	@Override
	public String toString() {
		return "AmountCoupon: minPurchaseAmount: " + String.format("%.2f", super.getMinPurchaseAmount())
				+ "TL, amount: " + String.format("%.2f", amount) + "TL";
	}

}