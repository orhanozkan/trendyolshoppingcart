import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.main.java.Category;
import com.main.java.IShoppingCart;
import com.main.java.Product;
import com.main.java.ShoppingCart;
import com.main.java.campaign.AmountCampaign;
import com.main.java.campaign.ICampaign;
import com.main.java.campaign.RateCampaign;
import com.main.java.coupon.AmountCoupon;
import com.main.java.coupon.ICoupon;
import com.main.java.coupon.RateCoupon;
import com.main.java.delivery.DeliveryCostCalculator;

public class ShoppingCartTest {

	private IShoppingCart shoppingCart;

	private Category category;
	private Category subcategory;
	private Product product;
	private Product subproduct;

	private Category category1;
	private Category subcategory1;
	private Category subcategory2;
	private Category category2;
	private Product product1;
	private Product product2;
	private Product product3;

	@Before
	public void setUp() {
		shoppingCart = new ShoppingCart(new DeliveryCostCalculator(4.0, 8.0));

		category = new Category("category");
		subcategory = new Category("subcategory", category);
		product = new Product("product", 1.0, category);
		subproduct = new Product("subproduct", 2.0, subcategory);

		category1 = new Category("category1");
		subcategory1 = new Category("subcategory1", category1);
		subcategory2 = new Category("subcategory2", category1);
		category2 = new Category("category2");

		product1 = new Product("product1", 5.0, subcategory1);
		product2 = new Product("product2", 3.0, subcategory2);
		product3 = new Product("product3", 20.0, category2);
	}


	@Test
	public void givenTwoProductsWithSingleCategoryWhenGetTotalAmountAfterDiscountCalledOnShoppingCartThenItShouldReturnTwoTimesProductPrice() {
		shoppingCart.addItem(product, 2);

		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(2.0, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWithOneProductWhenAddProductCalledWithSameProductAndQuantity1ThenAddProductReturns2() {
		shoppingCart.addItem(product, 1);

		int productQuantity = shoppingCart.addItem(product, 1);

		assertEquals(2, productQuantity);
	}

	@Test
	public void givenEmptyShoppingCartWhenAddProductCalledThenItShouldAddProduct() {
		shoppingCart.addItem(product, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyShoppingCartWhenAddProductCalledWithInvalidQuantityThenItShouldThrowInvalidArgumentException() {
		shoppingCart.addItem(product, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenShoppingCartWith1ProductWhenAddProductCalledWithInvalidQuantityThenItShouldThrowInvalidArgumentException() {
		shoppingCart.addItem(product, 1);

		shoppingCart.addItem(product, -1);
	}

	@Test
	public void givenShoppingCartWithProductsAndDiscountsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscountApplied() {
		ICampaign rateCampaign = new RateCampaign(category1, 25.0, 3);
		ICampaign amountCampaign = new AmountCampaign(subcategory1, 5.0, 2);

		shoppingCart.addItem(product1, 3);
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(product3, 5);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign, amountCampaign));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(112.0, totalAmount, 0.001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyShoppingCartWhenAddProductCalledWithNullProductThenItShouldThrowInvalidArgumentException() {
		shoppingCart.addItem(null, 1);
	}
	
	@Test
	public void givenProductsForSubcategoriesWithRateCampaignAppliedToParentCategoryWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscountApplied() {
		ICampaign rateCampaign = new RateCampaign(category, 20.0, 5);
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(subproduct, 3);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(8.0, totalAmount, 0.001);
	}

	@Test
	public void givenProductsLessThenMinimumProductQuantityForSubcategoriesWithRateCampaignAppliedToSubcategoryWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithoutDiscountApplied() {
		ICampaign rateCampaign = new RateCampaign(category, 20.0, 6);
		shoppingCart.addItem(product, 2);
		shoppingCart.addItem(subproduct, 3);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(8.0, totalAmount, 0.001);
	}


	@Test
	public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithAmountCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithoutDiscountApplied() {
		ICampaign amountCampaign = new AmountCampaign(category, 5.0, 6);
		shoppingCart.addItem(product, 5);
		shoppingCart.applyDiscounts(Arrays.asList(amountCampaign));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(5.0, totalAmount, 0.001);
	}
	
	@Test
	public void givenProductsForDifferentCategoriesWithoutAnyDiscountsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmount() {
		shoppingCart.addItem(product1, 2);
		shoppingCart.addItem(product2, 3);
		shoppingCart.addItem(product3, 5);

		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(119.0, totalAmount, 0.001);
	}

	@Test
	public void given2DifferentRateCampaignsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscountApplied() {
		ICampaign rateCampaign1 = new RateCampaign(category, 10.0, 5);
		ICampaign rateCampaign2 = new RateCampaign(category, 30.0, 5);
		shoppingCart.addItem(product, 5);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign1, rateCampaign2));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(5.0, totalAmount, 0.001);
	}

	@Test
	public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithRateCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithoutDiscountApplied() {
		ICampaign rateCampaign = new RateCampaign(category, 10.0, 6);
		shoppingCart.addItem(product, 5);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(5.0, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWithProductAndRateCouponWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscount() {
		shoppingCart.addItem(product, 10);

		ICoupon rateCoupon = new RateCoupon(10.0, 10.0);
		shoppingCart.applyCoupon(rateCoupon);

		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(10.0, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWithProductAndAmountCouponWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscount() {
		shoppingCart.addItem(product, 10);

		ICoupon amountCoupon = new AmountCoupon(10.0, 2.0);
		shoppingCart.applyCoupon(amountCoupon);

		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(10.0, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWithProductUnderMinPurchaseAmountAndAmountCouponWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithoutDiscount() {
		shoppingCart.addItem(product, 10);

		ICoupon amountCoupon = new AmountCoupon(20.0, 2.0);
		shoppingCart.applyCoupon(amountCoupon);

		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

		assertEquals(10.0, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWithProductOverMinPurchaseAmountAndAmountCouponWhenAddCouponCalledThenItShouldReturnTrue() {
		shoppingCart.addItem(product, 10);

		ICoupon amountCoupon = new AmountCoupon(10.0, 2.0);

		assertTrue(shoppingCart.applyCoupon(amountCoupon));
	}

	@Test
	public void givenEmptyShoppingCartWithAmountCouponWhenAddCouponCalledThenItShouldReturnFalse() {
		ICoupon amountCoupon = new AmountCoupon(10.0, 2.0);

		assertFalse(shoppingCart.applyCoupon(amountCoupon));
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenShoppingCartWithCampaignsAndCouponsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscounts() {
		ICampaign rateCampaign = new RateCampaign(category1, 25.0, 3);
		ICampaign amountCampaign = new AmountCampaign(subcategory1, 5.0, 2);
		shoppingCart.addItem(product1, 3);
		shoppingCart.addItem(product2, 2);
		shoppingCart.addItem(product3, 5);
		shoppingCart.applyDiscounts(Arrays.asList(rateCampaign, amountCampaign));
		ICoupon rateCoupon = new RateCoupon(10.0, 10.0);
		ICoupon amountCoupon = new AmountCoupon(8.0, 2.5);
		shoppingCart.applyCoupon(rateCoupon);
		shoppingCart.applyCoupon(amountCoupon);
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(97.175, totalAmount, 0.001);
	}

	@Test
	public void givenShoppingCartWhenGetDeliveryCostCalledThenItShouldReturnDeliveryCost() {

		Category category1 = new Category("category1");
		Category category2 = new Category("category2");

		Product product1 = new Product("product1", 5.0, category1);
		Product product2 = new Product("product2", 3.0, category1);
		Product product3 = new Product("product3", 20.0, category2);

		ShoppingCart shoppingCart = new ShoppingCart(new DeliveryCostCalculator(2.0, 3.0));
		shoppingCart.addItem(product1, 3);
		shoppingCart.addItem(product2, 2);
		shoppingCart.addItem(product3, 5);

		double deliveryCost = shoppingCart.getDeliveryCost();

		assertEquals(15.99, deliveryCost, 0.001);
	}

	@Test
	public void givenEmptyShoppingCartWhenGetDeliveryCostCalledThenItShouldReturnFixedCost() {
		assertEquals(2.99, shoppingCart.getDeliveryCost(), 0.001);
	}

	@Test
	public void given2DifferentAmountCampaignsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscountApplied() {
		ICampaign amountCampaign1 = new AmountCampaign(category, 1.0, 5);
		ICampaign amountCampaign2 = new AmountCampaign(category, 1.5, 5);
		shoppingCart.addItem(product, 5);
		shoppingCart.applyDiscounts(Arrays.asList(amountCampaign1, amountCampaign2));
		double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();
		assertEquals(5.0, totalAmount, 0.001);
	}

	@Test
	public void givenProductWhenToStringCalledThenReturnProduct() {
		assertEquals("     - product: unitPrice: 1,00 TL", product.toString());
	}

	@Test
	public void givenAmountCouponWhenToStringCalledThenReturnAmountCoupon() {
		ICoupon amountCoupon = new AmountCoupon(8.0, 2.5);
		assertEquals("AmountCoupon: minPurchaseAmount: 8,00TL, amount: 2,50TL", amountCoupon.toString());
	}

	@Test
	public void givenRateCouponWhenToStringCalledThenReturnRateCoupon() {
		ICoupon rateCoupon = new RateCoupon(10.0, 10.0);
		assertEquals("RateCoupon: minPurchaseAmount: 10,00TL, rate: %10.0", rateCoupon.toString());
	}

}