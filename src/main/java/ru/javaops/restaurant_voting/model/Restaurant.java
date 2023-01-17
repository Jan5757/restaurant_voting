package ru.javaops.restaurant_voting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.restaurant_voting.util.validation.NoHtml;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements Serializable {

    @NotBlank
    @Size(max = 256)
    @Column(name = "adress", nullable = false)
    @NoHtml
    private String adress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String adress) {
        super(id, name);
        this.adress = adress;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.adress);
    }
}
