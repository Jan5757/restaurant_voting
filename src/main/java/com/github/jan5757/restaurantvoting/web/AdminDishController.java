package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.repository.RestaurantRepository;
import com.github.jan5757.restaurantvoting.to.DishTo;
import com.github.jan5757.restaurantvoting.util.validation.ValidationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.jan5757.restaurantvoting.model.Dish;
import com.github.jan5757.restaurantvoting.repository.DishRepository;
import com.github.jan5757.restaurantvoting.util.DishUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants";
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(value = "/{restId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restId) {
        log.info("create {} in restaurant {}", dish, restId);
        ValidationUtil.checkNew(dish);
        dish.setRestaurant(restaurantRepository.getExisted(restId));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restId}/dishes/{id}").buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restId, @PathVariable int id) {
        log.info("update {} in restaurant {}", dish, restId);
        ValidationUtil.assureIdConsistent(dish, id);
        dishRepository.checkBelong(id, restId);
        dish.setRestaurant(restaurantRepository.getExisted(restId));
        dishRepository.save(dish);
    }

    @DeleteMapping("/{restId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete dish id {} from restaurant {}", id, restId);
        dishRepository.checkBelong(id, restId);
        dishRepository.delete(id);
    }

    @GetMapping("/{restId}/dishes")
    public List<DishTo> getAll(@PathVariable int restId) {
        log.info("getAll from restaurant {}", restId);
        return DishUtil.getTos(dishRepository.getAll(restId));
    }

    @GetMapping("/{restId}/dishes/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} from restaurant {}", id, restId);
        return ResponseEntity.of(dishRepository.get(id, restId));
    }

    @GetMapping("/{restId}/dishes/by-date")
    public List<DishTo> getAllByDate(@PathVariable int restId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate {} from restaurant {}", date, restId);
        return DishUtil.getTos(dishRepository.getAllByDate(restId, date));
    }
}
