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
import ru.javaops.restaurant_voting.util.validation.CurrencyCode;
import ru.javaops.restaurant_voting.util.validation.NoHtml;

import java.time.LocalDate;

@Entity
@Table(name = "dish", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "rest_id", "date"}, name = "dish_unique_name_rest_date_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dish extends NamedEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @NotNull
    @Range(min = 0, max = 999999999)
    @Column(name = "price", nullable = false)
    private Long price;

    @NotNull
    @Column(name = "currency_code_iso")
    @NoHtml
    @CurrencyCode
    private String currencyCode;


    @JsonIgnore
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Dish(Integer id, String name, LocalDate date, Long price, String currencyCode) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    public Dish(Dish d) {
        this(d.id, d.name, d.date, d.price, d.currencyCode);
    }
}
