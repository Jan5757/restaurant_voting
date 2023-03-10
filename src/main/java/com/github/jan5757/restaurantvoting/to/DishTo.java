package com.github.jan5757.restaurantvoting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import com.github.jan5757.restaurantvoting.model.NamedEntity;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedEntity {

    Long price;
    LocalDate date;

    public DishTo(Integer id, String name, Long price, LocalDate date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + price + ']';
    }
}
