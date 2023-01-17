package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.model.Restaurant;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");

    public static final int REST1_ID = 1;
    public static final int REST2_ID = 2;
    public static final int REST3_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final Restaurant rest1 = new Restaurant(REST1_ID, "Claude Monet", "25/20 building 1 Spiridonovka street 25/20b1, Moscow");
    public static final Restaurant rest2 = new Restaurant(REST2_ID, "Primavera", "17 Shabolovka Street, Moscow");
    public static final Restaurant rest3 = new Restaurant(REST3_ID, "Praga", "2/1 Arbat St, Moscow");

    static {
        rest1.setDishes(DishTestData.dishes);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New", "NewAdress");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(REST1_ID, "UpdatedName", "NewAdress");
    }
}
