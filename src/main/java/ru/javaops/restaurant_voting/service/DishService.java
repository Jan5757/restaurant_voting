package ru.javaops.restaurant_voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Dish;
import ru.javaops.restaurant_voting.repository.DishRepository;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Dish save(Dish dish, int restId) {
        dish.setRestaurant(restaurantRepository.getExisted(restId));
        return dishRepository.save(dish);
    }
}
