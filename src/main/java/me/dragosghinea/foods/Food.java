package me.dragosghinea.foods;

import me.dragosghinea.types.FoodType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Food {

    private BigDecimal price;
    private Long expirationDate;
    private String ingredients;
    private Double calories;
    private FoodType foodType;

    protected Food() {

    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public String toString() {
        return "Category: " + getFoodType() + "\n" +
                "Price: " + getPrice() + "\n" +
                "Calories: " + getCalories() + "\n" +
                "Expiration Date: " + new SimpleDateFormat("MM/dd/yyyy").format(new Date(expirationDate*1000)) + "\n" +
                "Ingredients: " + getIngredients() + "\n";
    }
}
