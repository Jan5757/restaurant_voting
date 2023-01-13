package ru.javaops.restaurant_voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javaops.restaurant_voting.model.BaseEntity;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;
import ru.javaops.restaurant_voting.repository.VoteRepository;
import ru.javaops.restaurant_voting.web.AuthUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    public boolean checkEnabled(int restId) {
        return restaurantRepository.getExisted(restId).isEnabled();
    }

    public void doVote(AuthUser authUser, LocalDate date, int restId) {
        Vote vote = new Vote(null, authUser.getUser(), date, restaurantRepository.getExisted(restId));
        prepareAndSaveVote(vote);
        voteRepository.save(vote);
    }

    public List<Vote> getAllVotesByDate(LocalDate date) {
        return voteRepository.getAllByDate(date);
    }

    public Optional<Restaurant> getWinner(LocalDate date) {
        List<Restaurant> restaurants = voteRepository.getWinnerByDate(date);
        return Optional.ofNullable(restaurants.isEmpty() ? null : restaurants.get(0));
    }

    public List<Restaurant> getAllEnabled() {
        return restaurantRepository.getAllEnabled();
    }

    public void prepareAndSaveVote(Vote vote) {
        Optional<Vote> currentVote = voteRepository.getVoteByDateAndUser(vote.getDate(), vote.getUser());
        vote.setId(currentVote.map(BaseEntity::getId).orElse(null));
        voteRepository.save(vote);
    }
}
