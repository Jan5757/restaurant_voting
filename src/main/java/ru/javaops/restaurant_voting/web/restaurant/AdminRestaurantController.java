package ru.javaops.restaurant_voting.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting.model.Restaurant;
import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.repository.RestaurantRepository;
import ru.javaops.restaurant_voting.service.RestaurantService;
import ru.javaops.restaurant_voting.util.validation.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository repository;
    private final RestaurantService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurantsEnabled", allEntries = true)
    })
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        ValidationUtil.checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurantsEnabled", allEntries = true)
    })
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        ValidationUtil.checkExisted(repository.get(id), id);
        repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict("restaurants")
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurantsEnabled", allEntries = true)
    })
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping
    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurantsEnabled", allEntries = true)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        Restaurant repositoryExisted = repository.getExisted(id);
        repositoryExisted.setEnabled(enabled);
    }

    @GetMapping("/votes")
    public List<Vote> getAllVotesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllVotesByDate");
        return service.getAllVotesByDate(date);
    }

    @GetMapping("/votes/winner")
    public ResponseEntity<Restaurant> getWinner(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getWinner Restaurant");
        return ResponseEntity.of(service.getWinner(date));
    }
}
