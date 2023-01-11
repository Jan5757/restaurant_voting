package ru.javaops.restaurant_voting.util;

import lombok.experimental.UtilityClass;
import ru.javaops.restaurant_voting.model.Dish;
import ru.javaops.restaurant_voting.model.Money;
import ru.javaops.restaurant_voting.to.DishTo;

import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DishUtil {
    public static final String TIME_VOTING_END = "11:00";

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream().map(DishUtil::createTo).collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), new Money(dish.getPrice(), Currency.getInstance(dish.getCurrencyCode())), dish.getDate());
    }
}
