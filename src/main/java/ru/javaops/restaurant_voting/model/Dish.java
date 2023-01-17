package ru.javaops.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date_menu", "name"}, name = "dish_unique_restaurant_date_menu_name_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "date_menu", nullable = false)
    @NotNull
    private LocalDate date;

    @NotNull
    @Range(min = 0)
    @Column(name = "price", nullable = false)
    private Long price;

    @JsonIgnore
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Dish(Integer id, String name, LocalDate date, Long price) {
        super(id, name);
        this.date = date;
        this.price = price;
    }

    public Dish(Dish d) {
        this(d.id, d.name, d.date, d.price);
    }
}
