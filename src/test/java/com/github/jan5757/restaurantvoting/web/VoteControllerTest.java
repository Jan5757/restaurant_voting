package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.model.Vote;
import com.github.jan5757.restaurantvoting.repository.VoteRepository;
import com.github.jan5757.restaurantvoting.util.TimeUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.*;

import static com.github.jan5757.restaurantvoting.web.UserTestData.USER_MAIL;
import static com.github.jan5757.restaurantvoting.web.UserTestData.user;
import static com.github.jan5757.restaurantvoting.web.VoteController.REST_URL;
import static com.github.jan5757.restaurantvoting.web.VoteTestData.VOTE_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';
    private static final LocalDateTime BEFORE_END_VOTING = LocalDateTime.of(2022, 1, 10, 9, 0);
    private static final LocalDateTime AFTER_END_VOTING = LocalDateTime.of(2022, 1, 10, 13, 0);

    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    public void before() {
        TimeUtil.setClock(Clock.systemDefaultZone());
    }

    @AfterEach
    public void after() {
        TimeUtil.setClock(Clock.systemDefaultZone());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateBeforeEnd() throws Exception {
        TimeUtil.setClock(Clock.fixed(BEFORE_END_VOTING.toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VoteTestData.VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restId", "3"))
                .andDo(print())
                .andExpect(status().isNoContent());
        Vote expected = new Vote(1, user, LocalDate.of(2022, Month.JANUARY, 10), RestaurantTestData.rest3);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(VoteTestData.VOTE1_ID), expected);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateAfterEnd() throws Exception {
        TimeUtil.setClock(Clock.fixed(AFTER_END_VOTING.toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VoteTestData.VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restId", "3"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("restId", "1"))
                .andExpect(status().isCreated());
        Vote expected = new Vote(null, user, LocalDate.now(), RestaurantTestData.rest1);
        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        expected.setId(newId);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), expected);
    }
}
