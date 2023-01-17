package com.github.jan5757.restaurantvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.jan5757.restaurantvoting.model.Dish;
import com.github.jan5757.restaurantvoting.repository.DishRepository;
import com.github.jan5757.restaurantvoting.util.DishUtil;
import com.github.jan5757.restaurantvoting.util.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.jan5757.restaurantvoting.web.AdminDishController.REST_URL;
import static com.github.jan5757.restaurantvoting.web.UserTestData.ADMIN_MAIL;
import static com.github.jan5757.restaurantvoting.web.UserTestData.USER_MAIL;

public class AdminDishControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + DishTestData.DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.dish1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + DishTestData.DISH1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DishTestData.DISH1_ID, RestaurantTestData.REST1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(dishRepository.getExisted(DishTestData.DISH1_ID), DishTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = DishTestData.DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDish);
        DishTestData.DISH_MATCHER.assertMatch(dishRepository.getExisted(newId), newDish);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_TO_MATCHER.contentJson(DishUtil.getTos(DishTestData.dishes)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, null, -1L);
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DishTestData.dish1);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish updated = new Dish(DishTestData.dish1);
        updated.setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes/" + DishTestData.DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RestaurantTestData.REST1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .param("date", "2022-01-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_TO_MATCHER.contentJson(DishUtil.getTos(DishTestData.dishes)));
    }
}
