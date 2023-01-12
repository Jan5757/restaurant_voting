package ru.javaops.restaurant_voting.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.model.User;
import ru.javaops.restaurant_voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.date=:date")
    List<Vote> getAllByDate(LocalDate date);

    @Query("SELECT v.restaurant FROM Vote v WHERE v.date=:date GROUP BY v.restaurant ORDER BY COUNT(v.restaurant) DESC")
    List<Restaurant> getWinnerByDate(LocalDate date);

    Optional<Vote> getVoteByDateAndUser(@NotNull LocalDate date, User user);
}
