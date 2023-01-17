package ru.javaops.restaurant_voting.web;

import ru.javaops.restaurant_voting.model.Vote;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestData {
    public static MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingAssertions(Vote.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("user.password", "user.registered", "restaurant.dishes").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });
    public static final int VOTE1_ID = 1;

    public static final Vote vote1 = new Vote(VOTE1_ID, UserTestData.user, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest1);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, UserTestData.admin, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest2);
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, UserTestData.user2, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest3);
}
