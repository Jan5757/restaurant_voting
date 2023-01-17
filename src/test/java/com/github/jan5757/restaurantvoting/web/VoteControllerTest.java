package com.github.jan5757.restaurantvoting.web;

import com.github.jan5757.restaurantvoting.model.Vote;
import com.github.jan5757.restaurantvoting.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.jan5757.restaurantvoting.web.UserTestData.USER_MAIL;
import static com.github.jan5757.restaurantvoting.web.UserTestData.user;
import static com.github.jan5757.restaurantvoting.web.VoteController.REST_URL;
import static com.github.jan5757.restaurantvoting.web.VoteTestData.VOTE_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {
//    private static final String REST_URL_SLASH = REST_URL + '/';
//    private static final LocalDate BEFORE_END_VOTING = LocalDate.of(2022, 1,10);
//    private static final LocalDateTime AFTER_END_VOTING = LocalDateTime.of(2022, 1,10,13, 0);
//    private Clock clock = Clock.fixed(BEFORE_END_VOTING.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Autowired
    private VoteRepository voteRepository;

//    @Test
//    @WithUserDetails(value = USER_MAIL)
//    void update() throws Exception {
//
//        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VOTE1_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("restId", "3"))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//
//        VOTE_MATCHER.assertMatch(voteRepository.getExisted(VOTE1_ID), vote1);
//    }

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
