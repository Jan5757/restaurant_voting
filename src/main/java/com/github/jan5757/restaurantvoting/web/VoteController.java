package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.error.IllegalRequestDataException;
import com.github.jan5757.restaurantvoting.model.Vote;
import com.github.jan5757.restaurantvoting.repository.RestaurantRepository;
import com.github.jan5757.restaurantvoting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    public static final LocalTime TIME_VOTING_END = LocalTime.of(11, 0);
    static final String REST_URL = "/api/votes";
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final Clock clock;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId) {
        log.info("voting user {} for restaurant {}", authUser, restId);
        LocalDateTime votingDateTime = LocalDateTime.now(clock);
        if (voteRepository.getVoteByDateAndUser(votingDateTime.toLocalDate(), authUser.getUser()).isPresent()) {
            throw new IllegalRequestDataException("User has already voted today. Please try update exist vote");
        }
        Vote vote = new Vote(null, authUser.getUser(), votingDateTime.toLocalDate(), restaurantRepository.getExisted(restId));
//        Vote vote = new Vote(null, authUser.getUser(), votingDateTime.toLocalDate(),
//                em.getReference(Restaurant.class, restId));
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @RequestParam int restId, @PathVariable int id) {
        log.info("update vote for user {}", authUser);
        LocalDateTime votingDateTime = LocalDateTime.now(clock);
        if (voteRepository.getVoteByDateAndUser(votingDateTime.toLocalDate(), authUser.getUser()).isEmpty()
                || votingDateTime.toLocalTime().isAfter(TIME_VOTING_END)) {
            throw new IllegalRequestDataException("Time to vote is over. Please vote until " + TIME_VOTING_END
                    + " or vote not exist");
        }
        Vote vote = new Vote(id, authUser.getUser(), votingDateTime.toLocalDate(), restaurantRepository.getExisted(restId));
        voteRepository.checkBelong(id, authUser.id());
        voteRepository.save(vote);
    }
}
