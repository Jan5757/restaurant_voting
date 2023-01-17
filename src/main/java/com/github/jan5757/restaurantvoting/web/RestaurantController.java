package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.jan5757.restaurantvoting.model.Restaurant;
import com.github.jan5757.restaurantvoting.util.validation.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";
    private final RestaurantRepository repository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
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
    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {}", restaurant);
        ValidationUtil.assureIdConsistent(restaurant, id);
        ValidationUtil.checkExisted(repository.get(id), id);
        repository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/{id}/with-dishes")
    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int id, LocalDate date) {
        log.info("getWithDishes restaurant {} by date {}", id, date);
        return ResponseEntity.of(repository.getWithDishes(id, date));
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping(value = "/with-dishes")
    @Cacheable("restaurants")
    public List<Restaurant> getAllWithDishesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllWithDishesByDate");
        return repository.getAllWithDishesByDate(date);
    }
}
