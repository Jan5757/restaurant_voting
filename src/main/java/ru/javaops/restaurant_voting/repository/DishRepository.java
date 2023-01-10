package ru.javaops.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.error.DataConflictException;
import ru.javaops.restaurant_voting.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restId")
    List<Dish> getAll(int restId);

    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurant.id = :restId")
    Optional<Dish> get(int id, int restId);

    default Dish checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}
