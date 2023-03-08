package me.dragosghinea.foods;

import me.dragosghinea.types.CerealType;
import me.dragosghinea.types.FoodType;

import java.math.BigDecimal;

public class Cereal extends Food{
    private CerealType cerealType;
    public Cereal(BigDecimal price, Long expirationDate, String ingredients, CerealType cerealType){
        setFoodType(FoodType.GRAINS);
        setCalories(2.56);

        this.cerealType=cerealType;
        setPrice(price);
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }

    public Cereal(Double price, Long expirationDate, String ingredients, CerealType cerealType){
        setFoodType(FoodType.GRAINS);
        setCalories(2.56);

        this.cerealType=cerealType;
        setPrice(BigDecimal.valueOf(price));
        setExpirationDate(expirationDate);
        setIngredients(ingredients);
    }

    public CerealType getCerealType() {
        return cerealType;
    }

    public void setCerealType(CerealType cerealType) {
        this.cerealType = cerealType;
    }
}
