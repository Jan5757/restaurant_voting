package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.model.Dish;
import com.github.jan5757.restaurantvoting.to.DishTo;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static final MatcherFactory.Matcher<DishTo> DISH_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(DishTo.class, "restaurant");

    public static final int DISH1_ID = 1;

    public static final Dish dish1 = new Dish(DISH1_ID, "Bouillabaisse", LocalDate.of(2022, Month.JANUARY, 10), 55000L);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Ratatouille", LocalDate.of(2022, Month.JANUARY, 10), 35000L);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "Salade nisoise", LocalDate.of(2022, Month.JANUARY, 10), 35000L);
    public static final Dish dish4 = new Dish(DISH1_ID + 3, "La creme brulee", LocalDate.of(2022, Month.JANUARY, 10), 20000L);
    public static final Dish dish5 = new Dish(DISH1_ID + 4, "Coffee", LocalDate.of(2022, Month.JANUARY, 10), 10000L);

    public static final List<Dish> dishes = List.of(dish1, dish5, dish4, dish2, dish3);

    public static Dish getNew() {
        return new Dish(null, "newName", dish1.getDate().plus(3, ChronoUnit.DAYS), 1000L);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "UpdatedName", dish1.getDate().plus(2, ChronoUnit.DAYS), 1000L);
    }
}
