package com.main.java.campaign;

import com.main.java.Category;

public interface ICampaign {

    boolean isApplicable(int productQuantity);

    double getDiscount(double totalPrice);
    
    Category getCategory();
}