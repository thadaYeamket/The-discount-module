package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class discountController {
    static class Item {
        String name;
        String category; // Clothing, Accessories, Electronics
        double price;

        Item(String name, String category, double price) {
            this.name = name;
            this.category = category;
            this.price = price;
        }
    }

    abstract static class DiscountCampaign {
        String category; // Coupon, OnTop, Seasonal

        abstract double apply(List<Item> items, double currentTotal);
    }

    // Coupon - Fixed Amount
    static class FixedAmountCoupon extends DiscountCampaign {
        double amount;

        FixedAmountCoupon(double amount) {
            this.category = "Coupon";
            this.amount = amount;
        }

        @Override
        double apply(List<Item> items, double currentTotal) {
            return Math.max(0, currentTotal - amount);
        }
    }

    // Coupon - Percentage
    static class PercentageCoupon extends DiscountCampaign {
        double percentage;

        PercentageCoupon(double percentage) {
            this.category = "Coupon";
            this.percentage = percentage;
        }

        @Override
        double apply(List<Item> items, double currentTotal) {
            return currentTotal * (1 - percentage / 100);
        }
    }

    // OnTop - Category Percentage Discount
    static class CategoryPercentageDiscount extends DiscountCampaign {
        String targetCategory;
        double percentage;

        CategoryPercentageDiscount(String targetCategory, double percentage) {
            this.category = "OnTop";
            this.targetCategory = targetCategory;
            this.percentage = percentage;
        }

        @Override
        double apply(List<Item> items, double currentTotal) {
            double discountAmount = 0;
            for (Item item : items) {
                if (item.category.equalsIgnoreCase(targetCategory)) {
                    discountAmount += item.price * (percentage / 100);
                }
            }
            return currentTotal - discountAmount;
        }
    }

    // OnTop - Discount by Points
    static class PointsDiscount extends DiscountCampaign {
        int points;

        PointsDiscount(int points) {
            this.category = "OnTop";
            this.points = points;
        }

        @Override
        double apply(List<Item> items, double currentTotal) {
            double maxDiscount = currentTotal * 0.2;
            double discount = Math.min(points, maxDiscount);
            return currentTotal - discount;
        }
    }

    // Seasonal Campaign
    static class SeasonalDiscount extends DiscountCampaign {
        double everyX;
        double discountY;

        SeasonalDiscount(double everyX, double discountY) {
            this.category = "Seasonal";
            this.everyX = everyX;
            this.discountY = discountY;
        }

        @Override
        double apply(List<Item> items, double currentTotal) {
            int applicableTimes = (int) (currentTotal / everyX);
            double totalDiscount = applicableTimes * discountY;
            return Math.max(0, currentTotal - totalDiscount);
        }
    }

    static class DiscountModule {
        List<Item> items;
        List<DiscountCampaign> campaigns;

        DiscountModule(List<Item> items, List<DiscountCampaign> campaigns) {
            this.items = items;
            this.campaigns = campaigns;
        }

        double calculateFinalPrice() {
            double total = items.stream().mapToDouble(i -> i.price).sum();

            Map<String, DiscountCampaign> selectedCampaigns = new HashMap<>();
            for (DiscountCampaign campaign : campaigns) {
                if (!selectedCampaigns.containsKey(campaign.category)) {
                    selectedCampaigns.put(campaign.category, campaign);
                } // ignore duplicate category campaign
            }

            List<String> applyOrder = Arrays.asList("Coupon", "OnTop", "Seasonal");
            for (String cat : applyOrder) {
                if (selectedCampaigns.containsKey(cat)) {
                    total = selectedCampaigns.get(cat).apply(items, total);
                }
            }
            return total;
        }
    }
}
