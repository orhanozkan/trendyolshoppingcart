package com.main.java.campaign;

import com.main.java.Category;

public class AmountCampaign extends Campaign {
	private double amount;

	public AmountCampaign(Category category, double amount, int minProductQuantity) {
		super(minProductQuantity, category);
		this.amount = amount;
	}

	@Override
	public double getDiscount(double totalAmount) {
		return amount;
	}

	@Override
	public String toString() {
		return "AmountCampaign: minProductQuantity: " + super.getMinProductQuantity() + ", amount: "
				+ String.format("%.2f", amount) + "TL";
	}
}