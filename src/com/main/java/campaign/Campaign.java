package com.main.java.campaign;

import com.main.java.Category;

public abstract class Campaign implements ICampaign {
	private int minProductQuantity;
	private Category category;

	public Campaign(int minProductQuantity, Category category) {
		this.minProductQuantity = minProductQuantity;
		this.category = category;
	}

	public int getMinProductQuantity() {
		return minProductQuantity;
	}

	public void setMinProductQuantity(int minProductQuantity) {
		this.minProductQuantity = minProductQuantity;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public boolean isApplicable(int productQuantity) {
		return productQuantity > minProductQuantity;
	}
}
