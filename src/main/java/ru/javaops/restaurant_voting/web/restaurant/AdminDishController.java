package ru.javaops.restaurant_voting.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting.model.Dish;
import ru.javaops.restaurant_voting.repository.DishRepository;
import ru.javaops.restaurant_voting.service.DishService;
import ru.javaops.restaurant_voting.to.DishTo;
import ru.javaops.restaurant_voting.util.DishUtil;
import ru.javaops.restaurant_voting.util.validation.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    static final String REST_URL = "/api/admin/restaurants";
    private final DishRepository repository;
    private final DishService service;

    @PostMapping(value = "/{restId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restId) {
        log.info("create {} in restaurant {}", dish, restId);
        ValidationUtil.checkNew(dish);
        Dish created = service.save(dish, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/" + restId + "/dishes/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restId, @PathVariable int id) {
        log.info("update {} in restaurant {}", dish, restId);
        ValidationUtil.assureIdConsistent(dish, id);
        repository.checkBelong(id, restId);
        service.save(dish, restId);
    }

    @DeleteMapping("/{restId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete dish id {} from restaurant {}", id, restId);
        repository.checkBelong(id, restId);
        repository.delete(id);
    }

    @GetMapping("/{restId}/dishes")
    public List<DishTo> getAll(@PathVariable int restId) {
        log.info("getAll from restaurant {}", restId);
        return DishUtil.getTos(repository.getAll(restId));
    }

    @GetMapping("/{restId}/dishes/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} from restaurant {}", id, restId);
        return ResponseEntity.of(repository.get(id, restId));
    }

    @GetMapping("/{restId}/dishes/by-date")
    public List<DishTo> getAllByDate(@PathVariable int restId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getAllByDate {} from restaurant {}", date, restId);
        return DishUtil.getTos(repository.getAllByDate(restId, date));
    }
}
