package ru.javaops.restaurant_voting.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.restaurant_voting.model.Money;
import ru.javaops.restaurant_voting.model.NamedEntity;

@Value
@EqualsAndHashCode(callSuper = true)
public class DishTo extends NamedEntity {

    Money price;

    public DishTo(Integer id, String name, Money price) {
        super(id, name);
        this.price = price;
    }
}
