package com.github.jan5757.restaurantvoting.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.jan5757.restaurantvoting.error.DataConflictException;
import com.github.jan5757.restaurantvoting.model.User;
import com.github.jan5757.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> getVoteByDateAndUser(@NotNull LocalDate date, User user);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> getByIdAndUserId(int id, int userId);

    default Vote checkBelong(int id, int userId) {
        return getByIdAndUserId(id, userId).orElseThrow(
                () -> new DataConflictException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }
}
