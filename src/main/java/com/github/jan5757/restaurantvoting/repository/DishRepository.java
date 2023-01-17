package com.github.jan5757.restaurantvoting.repository;

import com.github.jan5757.restaurantvoting.error.DataConflictException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.jan5757.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restId ORDER BY d.name")
    List<Dish> getAll(int restId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restId AND d.date=:date ORDER BY d.name")
    List<Dish> getAllByDate(int restId, LocalDate date);

    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurant.id = :restId")
    Optional<Dish> get(int id, int restId);

    default Dish checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}
