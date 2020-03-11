package com.main.java.delivery;

public class DeliveryCostCalculator implements IDeliveryCostCalculator {

    private static final double FIXED_COST = 2.99;

    private double costPerDelivery;
    private double costPerProduct;

    public DeliveryCostCalculator(double costPerDelivery, double costPerProduct) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
    }

    @Override
    public double calculateFor(int numberOfDeliveries, int numberOfProducts) {
        return numberOfDeliveries * costPerDelivery + numberOfProducts * costPerProduct + FIXED_COST;
    }
}