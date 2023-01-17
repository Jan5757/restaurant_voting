package com.github.jan5757.restaurantvoting.util;

import com.github.jan5757.restaurantvoting.to.DishTo;
import lombok.experimental.UtilityClass;
import com.github.jan5757.restaurantvoting.model.Dish;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DishUtil {

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream().map(DishUtil::createTo).collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice(), dish.getDate());
    }
}
