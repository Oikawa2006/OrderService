package ru.fomin.auth.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ru.fomin.auth.security.JwtTokenFilter;
import ru.fomin.auth.security.JwtTokenProvider;
import ru.fomin.auth.security.exception.UserNotFoundException;
import ru.fomin.auth.security.model.Role;
import ru.fomin.auth.security.model.Status;
import ru.fomin.auth.security.model.User;
import ru.fomin.auth.security.rest.UserRestController;
import ru.fomin.auth.security.rest.model.UserRequest;
import ru.fomin.auth.security.rest.model.UserResponse;
import ru.fomin.auth.security.service.UserService;
import ru.fomin.auth.util.DataUtilUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(value = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Test create user functionality")
    public void givenUserToSave_whenSaveUser_thenUserIsSaved() throws Exception {
        //given
        UserRequest userRequest = DataUtilUser.getUserRequestDanil();
        Mockito.when(userService.saveUser(any(UserRequest.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

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
    @DisplayName("Test update user functionality")
    public void givenSavedUser_whenUpdateUser_thenUserIsUpdated() throws Exception {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        UserRequest userRequest = DataUtilUser.getUserRequestDanilWithId();
        Mockito.when(userService.updateUser(any(UserRequest.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
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
    @DisplayName("Test update user functionality when user not found")
    public void givenSavedUser_whenUpdateUser_thenUserNotFoundException() throws Exception {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        UserRequest userRequest = DataUtilUser.getUserRequestDanilWithId();
        Mockito.when(userService.updateUser(any(UserRequest.class))).thenThrow(new UserNotFoundException("User not found"));
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
    }

    @Test
    @DisplayName("Test find by id user functionality")
    public void givenSavedUser_whenFindById_thenUserIsReturned() throws Exception {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.when(userService.findUserById(anyLong())).thenReturn(DataUtilUser.getUserResponseDanil());
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/" + user.getId())
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
    public void givenSavedUser_whenFindById_thenUserNotFoundException() throws Exception {
        //given
        Mockito.when(userService.findUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/-1")
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
    public void givenSavedUser_whenFindByEmail_thenUserIsReturned() throws Exception {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.when(userService.findUserByEmail(anyString())).thenReturn(DataUtilUser.getUserResponseDanil());
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/email/" + user.getEmail())
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
    @DisplayName("Test find by email user functionality when user not found")
    public void givenSavedUser_whenFindByEmail_thenUserNotFoundException() throws Exception {
        //given
        Mockito.when(userService.findUserByEmail(anyString())).thenThrow(new UserNotFoundException("User not found"));
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/email/-1")
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
    public void givenThreeUsers_whenFindAll_thenUsersIsReturned() throws Exception {
        //given
        User user1 = DataUtilUser.getUserDanilWithId();
        User user2 = DataUtilUser.getUserOlegWithId();
        User user3 = DataUtilUser.getUserDimaWithId();
        List<User> users = List.of(user1, user2, user3);
        UserResponse usersResponse1 = DataUtilUser.getUserResponseDanil();
        UserResponse usersResponse2 = DataUtilUser.getUserResponseOleg();
        UserResponse usersResponse3 = DataUtilUser.getUserResponseDima();
        List<UserResponse> userResponses = List.of(usersResponse1, usersResponse2, usersResponse3);
        Mockito.when(userService.findAllUsers()).thenReturn(userResponses);
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test delete user functionality")
    public void givenSavedUser_whenDeleteUser_thenUserIsDeleted() throws Exception {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.doNothing().when(userService).deleteUserById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService, Mockito.times(1)).deleteUserById(anyLong());
    }

    @Test
    @DisplayName("Test delete user functionality when user not found")

    public void givenSavedUser_whenDeleteUser_thenUserNotFoundException() throws Exception {
        //given
        Mockito.doThrow(new UserNotFoundException("User not found")).when(userService).deleteUserById(anyLong());
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/-1")
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("User not found")));
        ;
        Mockito.verify(userService, Mockito.times(1)).deleteUserById(anyLong());
    }


}
