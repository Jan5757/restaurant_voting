package com.github.jan5757.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    @JsonIgnore
    default boolean isNew() {
        return getId() == null;
    }

    default int id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}
