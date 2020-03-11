package com.main.java.campaign;

import com.main.java.Category;

public class RateCampaign extends Campaign {

	private double rate;

	public RateCampaign(Category category, double rate, int minProductQuantity) {
		super(minProductQuantity, category);
		this.rate = rate;
	}

	@Override
	public double getDiscount(double totalAmount) {
		return totalAmount * (rate / 100);
	}

	@Override
	public String toString() {
		return "RateCampaign: minProductQuantity: " + super.getMinProductQuantity() + ", rate: %" + rate;
	}
}