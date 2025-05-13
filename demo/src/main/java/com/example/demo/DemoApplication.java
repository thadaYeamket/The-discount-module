package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.discountController.CategoryPercentageDiscount;
import com.example.demo.discountController.DiscountCampaign;
import com.example.demo.discountController.DiscountModule;
import com.example.demo.discountController.FixedAmountCoupon;
import com.example.demo.discountController.Item;
import com.example.demo.discountController.SeasonalDiscount;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		List<Item> cart = Arrays.asList(
				new Item("T-Shirt", "Clothing", 350),
				new Item("Hat", "Accessories", 250),
				new Item("Belt", "Accessories", 230));

		List<DiscountCampaign> campaigns = Arrays.asList(
				new FixedAmountCoupon(50),
				new CategoryPercentageDiscount("Clothing", 15),
				new SeasonalDiscount(300, 40));

		DiscountModule module = new DiscountModule(cart, campaigns);
		double finalPrice = module.calculateFinalPrice();

		System.out.printf("Final price: %.2f THB\n", finalPrice);
	}

}
