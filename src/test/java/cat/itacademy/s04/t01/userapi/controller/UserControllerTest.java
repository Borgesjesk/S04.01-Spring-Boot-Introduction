package cat.itacademy.s04.t01.userapi.controller;

import cat.itacademy.s04.t01.userapi.model.User;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnEmptyListInitially() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createUser_returnsUserWithIdAndStatus201() throws Exception {
        User user = new User();
        user.setName("Lian Poo");
        user.setEmail("lp@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lian Poo"))
                .andExpect(jsonPath("$.email").value("lp@example.com"))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        User user = new User();
        user.setName("Anne May");
        user.setEmail("am@example.com");

        String response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anne May"))
                .andExpect(jsonPath("$.email").value("am@example.com"))
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getUserById_returnsNotFoundForMissingId() throws Exception {
        mockMvc.perform(get("/users/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_WithNameFilter_returnsFilteredUsers() throws Exception {
        User user1 = new User();
        user1.setName("Gin Ross");
        user1.setEmail("gm@example.com");

        User user2 = new User();
        user2.setName("Ted Smith");
        user2.setEmail("ts@example.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)));


        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)));

        mockMvc.perform(get("/users").param("name", "Gin Ross"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Gin Ross"));
    }

    @Test
    void getUsers_WithBlankNameFilter_returnsAllUsers() throws Exception {
        mockMvc.perform(get("/users").param("name", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createUser_withEmptyBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_withVMalformedJson_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserId_withInvalidUuidFormat_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/users/not-a-valid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsers_withNonMatchingName_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/users").param("name", "zszsz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}