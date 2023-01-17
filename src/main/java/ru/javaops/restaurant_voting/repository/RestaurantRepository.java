package ru.javaops.restaurant_voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @EntityGraph(attributePaths = "dishes", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Restaurant r JOIN FETCH Dish d WHERE d.date=:localDate")
    List<Restaurant> getAllWithDishesByDate(LocalDate localDate);

    @EntityGraph(attributePaths = "dishes", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM Restaurant r JOIN FETCH Dish d WHERE r.id=:restId AND d.date=:localDate")
    Optional<Restaurant> getWithDishes(int restId, LocalDate localDate);
}
