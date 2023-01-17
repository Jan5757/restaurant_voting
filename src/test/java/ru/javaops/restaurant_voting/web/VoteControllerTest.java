package ru.javaops.restaurant_voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurant_voting.model.Vote;
import ru.javaops.restaurant_voting.repository.VoteRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurant_voting.web.UserTestData.USER_MAIL;
import static ru.javaops.restaurant_voting.web.UserTestData.user;
import static ru.javaops.restaurant_voting.web.VoteController.REST_URL;
import static ru.javaops.restaurant_voting.web.VoteTestData.VOTE_MATCHER;

public class VoteControllerTest extends AbstractControllerTest {
//    private static final String REST_URL_SLASH = REST_URL + '/';
//    private static final LocalDateTime BEFORE_END_VOTING = LocalDateTime.of(2022, 1,10,9, 0);
//    private static final LocalDateTime AFTER_END_VOTING = LocalDateTime.of(2022, 1,10,13, 0);

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
