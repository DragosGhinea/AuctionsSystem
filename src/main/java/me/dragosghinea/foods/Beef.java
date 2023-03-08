package me.dragosghinea.foods;

import me.dragosghinea.types.FoodType;

import java.math.BigDecimal;

public class Beef extends Food{

    public Beef(BigDecimal price, Long expirationDate, String ingredients){
        setFoodType(FoodType.MEATS);
        setCalories(1.05);

        setPrice(price);
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }

    public Beef(Double price, Long expirationDate, String ingredients){
        setFoodType(FoodType.MEATS);
        setCalories(1.05);

        setPrice(BigDecimal.valueOf(price));
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }
}
