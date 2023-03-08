package me.dragosghinea.foods;

import me.dragosghinea.types.FoodType;

import java.math.BigDecimal;

public class Milk extends Food{

    public Milk(BigDecimal price, Long expirationDate, String ingredients){
        setFoodType(FoodType.DAIRY);
        setCalories(3.05);

        setPrice(price);
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }

    public Milk(Double price, Long expirationDate, String ingredients){
        setFoodType(FoodType.DAIRY);
        setCalories(3.05);

        setPrice(BigDecimal.valueOf(price));
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }

    @Override
    public String toString() {
        return "Food: Milk\n" + super.toString();
    }
}
