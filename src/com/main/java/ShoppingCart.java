package com.main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.main.java.campaign.ICampaign;
import com.main.java.coupon.ICoupon;
import com.main.java.delivery.IDeliveryCostCalculator;
import com.main.java.util.NumberUtils;

public class ShoppingCart implements IShoppingCart {
	private IDeliveryCostCalculator deliveryCostCalculator;
	private Map<Product, Integer> productMap;
	private List<ICoupon> coupons;
	private List<ICampaign> campaigns;
	private List<ICampaign> appliedCampaigns;
	private double couponDiscount;
	private double campaignDiscount;

	public ShoppingCart(IDeliveryCostCalculator deliveryCostCalculator) {
		this.deliveryCostCalculator = deliveryCostCalculator;
		this.productMap = new HashMap<>();
		this.coupons = new ArrayList<>();
		this.campaigns = new ArrayList<>();
	}

	@Override
	public int addItem(Product product, int quantity) {
		validateItem(product, quantity);
		if (!productMap.containsKey(product)) {
			productMap.put(product, quantity);
		} else {
			productMap.put(product, productMap.get(product) + quantity);
		}
		return productMap.get(product);
	}

	@Override
	public int removeItem(Product product, int quantity) {
		validateItem(product, quantity);
		if (productMap.containsKey(product)) {
			if (productMap.get(product) > quantity) {
				productMap.put(product, productMap.get(product) - quantity);
			} else {
				productMap.remove(product);
			}
		}
		return NumberUtils.zeroIfNull(productMap.get(product));
	}

	private void validateItem(Product product, int quantity) {
		if (product == null)
			throw new IllegalArgumentException("product can not be null");
		if (quantity <= 0)
			throw new IllegalArgumentException("invalid quantity: " + quantity);
	}

	@Override
	public boolean applyCoupon(ICoupon coupon) {
		validateCoupon();
		if (coupon.isApplicable(getTotalAmountAfterDiscounts())) {
			setCouponDiscount(getCouponDiscount(coupon));
			coupons.add(coupon);
			return true;
		}
		return false;
	}

	@Override
	public double getCouponDiscount() {
		return couponDiscount;
	}

	private void validateCoupon() {
		if (!coupons.isEmpty()) {
			throw new IllegalArgumentException("you have already applied coupon");
		}
	}

	private Set<Category> findAllCategories() {
		Set<Category> categories = new HashSet<>();
		for (Product product : productMap.keySet())
			categories.addAll(product.getCategory().getAllParents());
		return categories;
	}

	private List<Product> findProducts(Category category) {
		return productMap.keySet().stream().filter(product -> product.getCategory().inCategory(category))
				.collect(Collectors.toList());
	}

	private double getTotalCartAmount() {
		return calculateTotalPrice(new ArrayList<>(productMap.keySet()));
	}

	public double getCouponDiscount(ICoupon coupon) {
		return coupon.getDiscount(getTotalAmountAfterDiscounts());
	}

	@Override
	public double getTotalAmountAfterDiscounts() {
		return getTotalCartAmount() - campaignDiscount;
	}

	private Set<Category> getCategories() {
		Set<Category> categories = new HashSet<>();
		for (Product product : productMap.keySet()) {
			categories.add(product.getCategory());
		}
		return categories;
	}

	private int getNumberOfDeliveries() {
		return getCategories().size();
	}

	private int getNumberOfProducts() {
		return productMap.entrySet().size();
	}

	@Override
	public double getDeliveryCost() {
		return deliveryCostCalculator.calculateFor(getNumberOfDeliveries(), getNumberOfProducts());
	}

	private int calculateProductQuantity(List<Product> products) {
		return products.stream().mapToInt(product -> productMap.get(product)).sum();
	}

	private double calculateTotalPrice(List<Product> products) {
		return products.stream().mapToDouble(product -> productMap.get(product) * product.getPrice()).sum();
	}

	@Override
	public void print() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		String productsOutput = getProductsAsText();
		String campaignsOutput = getCampaignsAsText();
		String couponsOutput = getCouponsAsText();
		return "Products\n" + (productsOutput.isEmpty() ? "no products.\n" : productsOutput) + "Campaigns\n"
				+ (campaignsOutput.isEmpty() ? "no campaigns.\n" : campaignsOutput) + "Coupons\n"
				+ (couponsOutput.isEmpty() ? "no coupons.\n" : couponsOutput) + "Summary\n" + " - totalAmount: "
				+ String.format("%.2f", getTotalAmountAfterDiscounts()) + " TL\n" + " - totalCampaignDiscount: "
				+ String.format("%.2f", getCampaignDiscount()) + " TL\n" + " - totalCouponDiscount: "
				+ String.format("%.2f", getCouponDiscount()) + " TL\n" + " - deliveryCost: "
				+ String.format("%.2f", getDeliveryCost()) + " TL\n";
	}

	private String getProductsAsText() {
		StringBuilder text = new StringBuilder();
		List<Category> categories = new ArrayList<>(getCategories());
		for (Category category : categories)
			text.append(getProductCategoryText(category));
		return text.toString();
	}

	private String getProductCategoryText(Category category) {
		StringBuilder text = new StringBuilder();
		text.append(" - " + category + "\n");
		List<Product> products = findProducts(category);
		for (Product product : products)
			text.append(product + ", quantity: " + productMap.get(product) + ", totalPrice: "
					+ String.format("%.2f", (productMap.get(product) * product.getPrice())) + " TL\n");
		return text.toString();
	}

	private String getCampaignsAsText() {
		StringBuilder text = new StringBuilder();
		List<Category> categories = new ArrayList<>(findAllCategories());
		for (Category category : categories) {
			text.append(getCampaignCategoryText(category));
		}
		return text.toString();
	}

	private String getCampaignCategoryText(Category category) {
		StringBuilder text = new StringBuilder();
		List<Product> products = findProducts(category);
		int productQuantity = calculateProductQuantity(products);
		List<ICampaign> campaignsOfCategory = findCampaigns(appliedCampaigns, category, productQuantity);
		if (!campaignsOfCategory.isEmpty()) {
			text.append(" - " + category + "\n");
			for (ICampaign campaign : campaignsOfCategory) {
				text.append("  - " + campaign + "\n");
			}
		}
		return text.toString();
	}

	private String getCouponsAsText() {
		StringBuilder text = new StringBuilder();
		for (ICoupon coupon : coupons)
			text.append(" - " + coupon.toString() + "\n");
		return text.toString();
	}

	@Override
	public void applyDiscounts(List<ICampaign> campaignList) {
		this.campaigns.addAll(campaignList);
		campaignDiscount = 0;
		appliedCampaigns = new ArrayList<>();
		for (Category category : findAllCategories()) {
			double maxCampaignDiscount = 0;
			List<Product> products = findProducts(category);
			int productQuantity = calculateProductQuantity(products);
			ICampaign appliedCampaign = null;
			for (ICampaign campaign : findCampaigns(campaignList, category, productQuantity)) {
				double discount = campaign.getDiscount(calculateTotalPrice(products));
				if (discount > maxCampaignDiscount) {
					maxCampaignDiscount = discount;
					appliedCampaign = campaign;
				}

			}
			if (appliedCampaign != null)
				appliedCampaigns.add(appliedCampaign);
			campaignDiscount += maxCampaignDiscount;
		}
	}

	private List<ICampaign> findCampaigns(List<ICampaign> campaignList, Category category, int productQuantity) {
		return campaignList.stream()
				.filter(campaign -> campaign.getCategory().equals(category) && campaign.isApplicable(productQuantity))
				.collect(Collectors.toList());
	}

	public void setCouponDiscount(double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public void setCampaignDiscount(double campaignDiscount) {
		this.campaignDiscount = campaignDiscount;
	}

	public double getCampaignDiscount() {
		return campaignDiscount;
	}
}