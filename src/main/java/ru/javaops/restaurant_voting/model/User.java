package ru.javaops.restaurant_voting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//todo Table
public class User extends NamedEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Email
    @NotBlank
    @Size(max = 128)
    //todo table, nohtml
    private String email;

    @NotBlank
    @Size(max = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //todo table
    private String password;

    //todo table
    private boolean enabled = true;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //todo table
    private Date registered = new Date();

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    //todo table
    private Set<Role> roles;

    //todo table
    private boolean isVoted = false;

    //todo table
    //schema?
    private Integer restaurantId;

//    public User(User u) {
//        this(u.id, u.name, u.email, u.password, u.enabled, u.registered, u.restaurantId, u.isVoted, u.roles);
//    }
//
//    public User(Integer id, String name, String email, String password, Integer restaurantId, boolean isVoted, Role... roles) {
//        this(id, name, email, password, true, new Date(), restaurantId, isVoted, Arrays.asList(roles));
//    }

    public User(Integer id, String name, String email, String password, boolean enabled, Date registered, Collection<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    @Override
    public String toString() {
        return "User:" + id + '[' + email + ']';
    }
}
