package ru.fomin.auth.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fomin.auth.security.exception.UserNotFoundException;
import ru.fomin.auth.security.mapper.UserMapper;
import ru.fomin.auth.security.model.User;
import ru.fomin.auth.security.repository.UserRepository;
import ru.fomin.auth.security.rest.model.UserRequest;
import ru.fomin.auth.security.rest.model.UserResponse;
import ru.fomin.auth.security.service.UserServiceImpl;
import ru.fomin.auth.util.DataUtilUser;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test save user functionality")
    public void givenUserToSave_whenSaveUser_thenUserIsSaved() {
        //given
        UserRequest userRequest = DataUtilUser.getUserRequestDanil();
        Mockito.when(userMapper.map(any(UserRequest.class))).thenReturn(DataUtilUser.getUserDanil());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(DataUtilUser.getUserDanilWithId());
        Mockito.when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        //when
        UserResponse userResponse = userService.saveUser(userRequest);
        //then
        Assertions.assertNotNull(userResponse);
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertNotNull(userResponse.getEmail());
        Assertions.assertNotNull(userResponse.getUsername());
        Assertions.assertNotNull(userResponse.getPassword());
        Assertions.assertNotNull(userResponse.getRole());
        Assertions.assertNotNull(userResponse.getStatus());
        Mockito.verify(userMapper,Mockito.times(1)).map(any(UserRequest.class));
        Mockito.verify(userRepository,Mockito.times(1)).save(any(User.class));
        Mockito.verify(userMapper,Mockito.times(1)).map(any(User.class));
    }

    @Test
    @DisplayName("Test update user functionality")
    public void givenSavedUser_whenUpdateUser_thenUserIsUpdated() {
        UserRequest userRequest = DataUtilUser.getUserRequestDanilWithId();
        Mockito.lenient().when(userMapper.map(any(UserRequest.class))).thenReturn(DataUtilUser.getUserDanilWithId());
        Mockito.lenient().when(userRepository.save(any(User.class))).thenReturn(DataUtilUser.getUserDanilWithId());
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
        //when
        UserResponse userResponse = userService.updateUser(userRequest);
        //then
        Assertions.assertNotNull(userResponse);
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertNotNull(userResponse.getEmail());
        Assertions.assertNotNull(userResponse.getUsername());
        Assertions.assertNotNull(userResponse.getPassword());
        Assertions.assertNotNull(userResponse.getRole());
        Assertions.assertNotNull(userResponse.getStatus());
        Mockito.verify(userMapper,Mockito.times(2)).map(any(UserRequest.class));
        Mockito.verify(userRepository,Mockito.times(1)).save(any(User.class));
        Mockito.verify(userMapper,Mockito.times(1)).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test update user functionality when user not found")
    public void givenSavedUser_whenUpdateUser_thenUserIsNotFound() {
        UserRequest userRequest = DataUtilUser.getUserRequestDanilWithId();
        Mockito.lenient().when(userMapper.map(any(UserRequest.class))).thenReturn(DataUtilUser.getUserDanilWithId());
        Mockito.lenient().when(userRepository.save(any(User.class))).thenReturn(DataUtilUser.getUserDanilWithId());
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(false);
        //when
        Assertions.assertThrows(UserNotFoundException.class,()->userService.updateUser(userRequest));
        //then
        Mockito.verify(userMapper,Mockito.times(1)).map(any(UserRequest.class));
        Mockito.verify(userRepository,Mockito.never()).save(any(User.class));
        Mockito.verify(userMapper,Mockito.never()).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Test find by id user functionality")
    public void givenSavedUser_whenFindById_thenUserIsReturned() {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //when
        UserResponse userResponse = userService.findUserById(user.getId());
        //then
        Assertions.assertNotNull(userResponse);
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertNotNull(userResponse.getEmail());
        Assertions.assertNotNull(userResponse.getUsername());
        Assertions.assertNotNull(userResponse.getPassword());
        Assertions.assertNotNull(userResponse.getRole());
        Assertions.assertNotNull(userResponse.getStatus());
        Mockito.verify(userMapper,Mockito.times(1)).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test find by id user functionality when user not found")
    public void givenSavedUser_whenFindById_thenUseNotFoundException() {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(UserNotFoundException.class,()->userService.findUserById(user.getId()));
        //then
        Mockito.verify(userMapper,Mockito.never()).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Test find by email user functionality")
    public void givenSavedUser_whenFindByEmail_thenUserIsReturned() {
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        //when
        UserResponse userResponse = userService.findUserByEmail(user.getEmail());
        //then
        Assertions.assertNotNull(userResponse);
        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertNotNull(userResponse.getEmail());
        Assertions.assertNotNull(userResponse.getUsername());
        Assertions.assertNotNull(userResponse.getPassword());
        Assertions.assertNotNull(userResponse.getRole());
        Assertions.assertNotNull(userResponse.getStatus());
        Mockito.verify(userMapper,Mockito.times(1)).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test find by email user functionality when user not found")
    public void givenSavedUser_whenFindByEmail_thenUseNotFoundException() {
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.lenient().when(userMapper.map(any(User.class))).thenReturn(DataUtilUser.getUserResponseDanil());
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        //when
        Assertions.assertThrows(UserNotFoundException.class,()->userService.findUserByEmail(user.getEmail()));
        //then
        Mockito.verify(userMapper,Mockito.never()).map(any(User.class));
        Mockito.verify(userRepository,Mockito.times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Test find all users functionality")
    public void givenThreeSavedUser_whenFindAll_thenUsersIsReturned() {
        //given
        User user1 = DataUtilUser.getUserDanilWithId();
        User user2 = DataUtilUser.getUserOlegWithId();
        User user3 = DataUtilUser.getUserDimaWithId();
        List<User> users = List.of(user1,user2,user3);
        UserResponse userResponse1 = DataUtilUser.getUserResponseDanil();
        UserResponse userResponse2 = DataUtilUser.getUserResponseOleg();
        UserResponse userResponse3 = DataUtilUser.getUserResponseDima();
        List<UserResponse> userResponses = List.of(userResponse1,userResponse2,userResponse3);
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userMapper.map(users)).thenReturn(userResponses);
        //when
        List<UserResponse> responses = userService.findAllUsers();
        //then
        Assertions.assertNotNull(responses);
        Assertions.assertEquals(responses.size(),3);
        Mockito.verify(userRepository,Mockito.times(1)).findAll();
        Mockito.verify(userMapper,Mockito.times(1)).map(users);
    }

    @Test
    @DisplayName("Test delete user functionality")
    public void givenSavedUser_whenDeleteUser_thenUserIsDeleted(){
        //given
        User user = DataUtilUser.getUserDanilWithId();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //when
        userService.deleteUserById(user.getId());
        //
        Mockito.verify(userRepository,Mockito.times(1)).deleteById(anyLong());
        Mockito.verify(userRepository,Mockito.times(1)).findById(anyLong());
    }
    @Test
    @DisplayName("Test delete user functionality when user not found")
    public void givenNotSavedUser_whenDeleteUser_thenUseNotFoundException(){
        //given
        //when
        Assertions.assertThrows(UserNotFoundException.class,()-> userService.deleteUserById(anyLong()));
        //then
        Mockito.verify(userRepository,Mockito.never()).deleteById(anyLong());
        Mockito.verify(userRepository,Mockito.times(1)).findById(anyLong());
    }

}
