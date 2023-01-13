package ru.javaops.restaurant_voting.web.restaurant;

import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.web.MatcherFactory;
import ru.javaops.restaurant_voting.web.user.UserTestData;

import java.time.LocalDate;
import java.time.Month;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final int VOTE1_ID = 1;

    public static final Vote vote1 = new Vote(VOTE1_ID, UserTestData.user, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest1);
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, UserTestData.admin, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest2);
    public static final Vote vote3 = new Vote(VOTE1_ID + 2, UserTestData.user2, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest3);
}
