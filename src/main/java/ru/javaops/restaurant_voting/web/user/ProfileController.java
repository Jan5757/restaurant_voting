package ru.javaops.restaurant_voting.web.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting.error.IllegalRequestDataException;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.model.User;
import ru.javaops.restaurant_voting.service.RestaurantService;
import ru.javaops.restaurant_voting.to.UserTo;
import ru.javaops.restaurant_voting.util.DishUtil;
import ru.javaops.restaurant_voting.util.UsersUtil;
import ru.javaops.restaurant_voting.util.validation.ValidationUtil;
import ru.javaops.restaurant_voting.web.AuthUser;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";
    private final RestaurantService restaurantService;

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "users", allEntries = true)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        ValidationUtil.assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }

    @PostMapping(value = "/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void voting(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId) {
        log.info("voting user {} for restaurant {}", authUser, restId);
        LocalDateTime votingDateTime = LocalDateTime.now();
        if (!restaurantService.checkEnabled(restId)) {
            throw new IllegalRequestDataException("Restaurant " + restId + " is not enabled");
        }
        if (votingDateTime.toLocalTime().isAfter(LocalTime.parse(DishUtil.TIME_VOTING_END))) {
            throw new IllegalRequestDataException("Time to vote is over. Please vote until " + DishUtil.TIME_VOTING_END);
        }
        restaurantService.doVote(authUser, votingDateTime.toLocalDate(), restId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "users", allEntries = true)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        ValidationUtil.checkNew(userTo);
        User created = prepareAndSave(UsersUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/restaurants/enabled")
    @Cacheable("restaurantsEnabled")
    public List<Restaurant> getAllEnabled() {
        log.info("getAllEnabled");
        return restaurantService.getAllEnabled();
    }
}
