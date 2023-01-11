package ru.javaops.restaurant_voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.date=:date")
    List<Vote> getAllByDate(LocalDate date);

    @Query("SELECT v.restaurant FROM Vote v WHERE v.date=:date GROUP BY v.restaurant ORDER BY COUNT(v.restaurant) DESC")
    List<Restaurant> getWinnerByDate(LocalDate date);
}
