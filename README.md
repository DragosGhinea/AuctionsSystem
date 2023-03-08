# Lab 3 Exercise
At the end of the 3rd laboratory of PAO (Programare Avansată pe Obiecte/Advanced Object-Oriented Programming) the following exercise was proposed.

## Requirements

Design a food system considering the following:

- It must have the following foods: Beef, Milk, Cereal
- Those foods must have a category (Dairy, Meats, Grains)

Foods have the following fields: price, expiration date, ingredients, calories, food type. Beef has 1.05 calories, milk has 3.05 and cereal has 2.56.

**Use as much OOP as possible**, have a single file for a single class, consider encapsulation (private fields), have constructors, getters, and setters (generated automatically by IntelliJ).

Cereal also has an extra field: cerealType (Nesquik, Chocapic)

1. Design the classes.
2. Create a list in Main with some foods. (Use List.of(...) to create a list, or List<Food> foods = new ArrayList<>(); and use foods.add(object)) -> to have access to the method foods.stream())
3. Sort using stream() by the number of calories.

## Solution

1. The classes were designed as shown in the below diagram.

![ClassDesign](https://github.com/DragosGhinea/AuctionsSystem/blob/Lab3/ClassDesign.svg)

2. Created the list using List.of

_-- To be continued --_
