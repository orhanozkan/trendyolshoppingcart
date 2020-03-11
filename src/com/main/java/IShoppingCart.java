package com.main.java;

import java.util.List;

import com.main.java.campaign.ICampaign;
import com.main.java.coupon.ICoupon;

public interface IShoppingCart {
	int addItem(Product product, int quantity);
	
	int removeItem(Product product, int quantity);
	
	void print();
	
	boolean applyCoupon(ICoupon coupon);

	void applyDiscounts(List<ICampaign> campaignList);

	double getTotalAmountAfterDiscounts();

	double getDeliveryCost();

	double getCouponDiscount();
}