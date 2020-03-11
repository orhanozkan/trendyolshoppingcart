package com.main.java;

import java.util.Arrays;

import com.main.java.campaign.AmountCampaign;
import com.main.java.campaign.ICampaign;
import com.main.java.campaign.RateCampaign;
import com.main.java.coupon.ICoupon;
import com.main.java.coupon.RateCoupon;
import com.main.java.delivery.DeliveryCostCalculator;

public class Main {

	public static void main(String[] args) {
		IShoppingCart shoppingCart = new ShoppingCart(new DeliveryCostCalculator(4.0, 8.0));

		Category category1 = new Category("Electronic");
		Category subcategory1 = new Category("Laptop", category1);
		Category subcategory2 = new Category("Desktop", category1);
		Category category2 = new Category("Automobile");
		Category subcategory3 = new Category("Honda", category2);
		Category subcategory4 = new Category("Toyota", category2);
		Category category3 = new Category("Headphone");
		
		ICampaign campaign1 = new RateCampaign(category1, 30.0, 10);
		ICampaign campaign4 = new RateCampaign(category1, 40, 11);
		ICampaign campaign2 = new AmountCampaign(category2, 15.0, 8);
		ICampaign campaign3 = new AmountCampaign(category2, 15.0, 8);

		Product product1 = new Product("Asus", 2.0, subcategory1);
		Product product2 = new Product("Casper", 4.0, subcategory2);
		Product product3 = new Product("Civic", 60.0, subcategory3);
		Product product4 = new Product("Corolla", 30.0, subcategory4);
		Product product5 = new Product("JBL", 3.0, category3);

		shoppingCart.addItem(product1, 3);
		shoppingCart.addItem(product2, 7);
		shoppingCart.addItem(product3, 5);
		shoppingCart.addItem(product4, 1);
		shoppingCart.addItem(product5, 10);

		shoppingCart.applyDiscounts(Arrays.asList(campaign1, campaign2, campaign3, campaign4));
		ICoupon rateCoupon = new RateCoupon(10.0, 10.0);
		shoppingCart.applyCoupon(rateCoupon);
		shoppingCart.print();
	}
}
