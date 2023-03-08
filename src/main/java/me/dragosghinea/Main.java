package me.dragosghinea;

import me.dragosghinea.foods.Beef;
import me.dragosghinea.foods.Cereal;
import me.dragosghinea.foods.Food;
import me.dragosghinea.foods.Milk;
import me.dragosghinea.types.CerealType;

import java.time.ZonedDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        List<Food> foodList = List.of(
                new Beef(10d, now.plusWeeks(2).toEpochSecond(), "Just old regular meat"),
                new Cereal(5d, now.plusMonths(1).toEpochSecond(), "Cereal, protein, other stuff", CerealType.NESQUIK),
                new Cereal(4.67d, now.plusMonths(1).toEpochSecond(), "Chocolate cips", CerealType.CHOCAPIC),
                new Milk(1.22d, now.plusDays(5).toEpochSecond(), "99% Milk, 1% Who Knows")
        );


    }
}
