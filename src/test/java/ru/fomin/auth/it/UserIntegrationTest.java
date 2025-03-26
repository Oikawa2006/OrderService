package ru.fomin.auth.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.fomin.auth.security.JwtTokenProvider;
import ru.fomin.auth.security.model.Role;
import ru.fomin.auth.security.model.Status;
import ru.fomin.auth.security.model.User;
import ru.fomin.auth.security.repository.UserRepository;
import ru.fomin.auth.security.rest.model.UserRequest;
import ru.fomin.auth.security.rest.model.UserResponse;
import ru.fomin.auth.util.DataUtilUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test create user functionality")
    @WithMockUser(authorities = "write")
    public void givenUserToSave_whenSaveUser_thenUserIsSaved() throws Exception {
        //given
        UserRequest userRequest = DataUtilUser.getUserRequestDanil();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(objectMapper.writeValueAsString(userRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("danil@mail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(Role.USER.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(Status.ACTIVE.name())));
    }

    @Test
    @DisplayName("Test update user functionality")
    @WithMockUser(authorities = "write")
    public void givenSavedUser_whenUpdateUser_thenUserIsUpdated() throws Exception {
        //given
        User user = DataUtilUser.getUserDanil();
        userRepository.save(user);
        UserRequest userRequest = DataUtilUser.getUserRequestDanil().setId(user.getId());
        String updatedEmail = "newEmail";
        userRequest.setEmail(updatedEmail);
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmail)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(Role.USER.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(Status.ACTIVE.name())));

    }

    @Test
    @DisplayName("Test update user functionality when user not found")
    @WithMockUser(authorities = "write")
    public void givenSavedUser_whenUpdateUser_thenUserNotFoundException() throws Exception {
        //given
        UserRequest userRequest = DataUtilUser.getUserRequestDanil().setId(1L);
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
    }
    @Test
    @DisplayName("Test find by id user functionality")
    @WithMockUser(authorities = "read")
    public void givenSavedUser_whenFindById_thenUserIsReturned() throws Exception {
        //given
        User user = DataUtilUser.getUserDanil();
        userRepository.save(user);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/{id}",user.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("danil@mail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(Role.USER.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(Status.ACTIVE.name())));
    }

    @Test
    @DisplayName("Test find by id user functionality when user not found")
    @WithMockUser(authorities = "read")
    public void givenSavedUser_whenFindById_thenUserNotFoundException() throws Exception {
        //given;
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/{id}",-1L)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
    }

    @Test
    @DisplayName("Test find by email functionality")
    @WithMockUser(authorities = "read")
    public void givenSavedUser_whenFindByEmail_thenUserIsReturned() throws Exception {
        //given
        User user = DataUtilUser.getUserDanil();
        userRepository.save(user);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/email/{email}",user.getEmail())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is("danil@mail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is("danil")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(Role.USER.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(Status.ACTIVE.name())));
    }
    @Test
    @DisplayName("Test find by email user functionality when user not found")
    @WithMockUser(authorities = "read")
    public void givenSavedUser_whenFindByEmail_thenUserNotFoundException() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/email/{email}",-1L)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
    }
    @Test
    @DisplayName("Test find all users functionality")
    @WithMockUser(authorities = "read")
    public void givenThreeUsers_whenFindAll_thenUsersIsReturned() throws Exception {
        //given
        User user1 = DataUtilUser.getUserDanil();
        User user2 = DataUtilUser.getUserOleg();
        User user3 = DataUtilUser.getUserDima();
        List<User> users = List.of(user1,user2,user3);
        userRepository.saveAll(users);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Test delete user functionality")
    @WithMockUser(authorities = "write")
    public void givenSavedUser_whenDeleteUser_thenUserIsDeleted() throws Exception {
        //given
        User user = DataUtilUser.getUserDanil();
        userRepository.save(user);
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Test delete user functionality when user not found")
    @WithMockUser(authorities = "write")
    public void givenSavedUser_whenDeleteUser_thenUserNotFoundException() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/-1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));

    }

}
