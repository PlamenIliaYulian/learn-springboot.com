package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.exceptions.DuplicateEntityException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Avatar;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.UserRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.AvatarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository userRepository;
    @Mock
    AvatarService avatarService;
    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void updateUser_Should_UpdateUser_When_TheSameUserTriesToUpdateTheirProfile() {
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserByEmail(Mockito.anyString()))
                .thenThrow(new EntityNotFoundException("User", "email", userToBeUpdated.getEmail()));

        userService.updateUser(userToBeUpdated, userToBeUpdated);

        Mockito.verify(userRepository, Mockito.times(1)).updateUser(userToBeUpdated);
    }

    @Test
    public void updateUser_Should_Throw_When_UnauthorizedUserTriesToUpdate() {
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        User userThatCannotUpdate = TestHelpers.createMockNoAdminUser();
        userThatCannotUpdate.setUserId(100);

        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> userService.updateUser(userToBeUpdated, userThatCannotUpdate));
    }

    @Test
    public void getUserByUsername_Should_ReturnUser_When_EverythingIsOk() {
        String mockUserNameByWhichWeSearchUser = "Mock_NoAdmin_User";

        Mockito.when(userRepository.getUserByUsername(mockUserNameByWhichWeSearchUser))
                .thenReturn(TestHelpers.createMockNoAdminUser());

        userService.getUserByUsername(mockUserNameByWhichWeSearchUser);

        Mockito.verify(userRepository, Mockito.times(1))
                .getUserByUsername(mockUserNameByWhichWeSearchUser);
    }

    @Test
    public void addAvatar_Should_ThrowEntityNotFoundException_When_ThereIsNoUserWithProvidedUserID() {
        User userToUpdateAvatarTo = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserById(userToUpdateAvatarTo.getUserId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.addAvatar(userToUpdateAvatarTo.getUserId(), null, userToUpdateAvatarTo));
    }

    @Test
    public void addAvatar_Should_ThrowUnauthorizedOperationException_When_TheUserTryingToUpdateAvatarIsNotSame() {
        User userToUpdateAvatarTo = TestHelpers.createMockNoAdminUser();
        User unauthorizedUser = TestHelpers.createMockNoAdminUser();
        unauthorizedUser.setUserId(777);

        Mockito.when(userRepository.getUserById(userToUpdateAvatarTo.getUserId()))
                .thenReturn(unauthorizedUser);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.addAvatar(userToUpdateAvatarTo.getUserId(), null, userToUpdateAvatarTo));
    }

    @Test
    public void makeAdministrativeChanges_Should_Pass_When_TheUserTryingToMakeTheChangesIsAdmin() {
        User adminUser = TestHelpers.createMockAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.makeAdministrativeChanges(userToBeUpdated))
                .thenReturn(userToBeUpdated);

        userService.makeAdministrativeChanges(adminUser, userToBeUpdated);

        Mockito.verify(userRepository, Mockito.times(1))
                .makeAdministrativeChanges(userToBeUpdated);
    }

    @Test
    public void makeAdministrativeChanges_Should_Throw_When_UserTryingToMakeChangesIsNotAdmin() {
        User nonAdminUser = TestHelpers.createMockNoAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.makeAdministrativeChanges(nonAdminUser, userToBeUpdated));
    }

    @Test
    public void createUser_Should_CallRepository() {
        User userToBeCreated = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserByUsername(userToBeCreated.getUserName()))
                .thenThrow(new EntityNotFoundException("User", "username", userToBeCreated.getUserName()));

        Mockito.when(userRepository.getUserByEmail(Mockito.anyString()))
                .thenThrow(new EntityNotFoundException("User", "email", userToBeCreated.getEmail()));

        Mockito.when(avatarService.getDefaultAvatar())
                .thenReturn(new Avatar());

        userService.createUser(userToBeCreated);

        Mockito.verify(userRepository, Mockito.times(1))
                .createUser(Mockito.any(User.class));
    }

    @Test
    public void createUser_Should_Throw_When_UsernameExists() {
        User existingUser = TestHelpers.createMockNoAdminUser();
        User userToBeCreated = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.createUser(userToBeCreated));
    }

    @Test
    public void createUser_Should_Throw_When_EmailExists() {
        User existingUser = TestHelpers.createMockNoAdminUser();
        User userToBeCreated = TestHelpers.createMockNoAdminUser();
        existingUser.setUserName("some new username");

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.createUser(userToBeCreated));
    }

    @Test
    public void getAllUser_Should_Throw_When_UserIsNotAdmin() {
        UserFilterOptions userFilterOptions = TestHelpers.createUserFilterOptions();
        User nonAdminUser = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getAllUsers(nonAdminUser, userFilterOptions));
    }

    @Test
    public void getAllUsers_Should_Pass_When_Valid() {
        User adminUser = TestHelpers.createMockAdminUser();
        List<User> users = new ArrayList<>();
        users.add(TestHelpers.createMockNoAdminUser());
        UserFilterOptions userFilterOptions = TestHelpers.createUserFilterOptions();

        Mockito.when(userRepository.getAllUsers(userFilterOptions))
                .thenReturn(users);

        List<User> allUsers = userService.getAllUsers(adminUser, userFilterOptions);

        Mockito.verify(userRepository, Mockito.times(1))
                .getAllUsers(userFilterOptions);
        Assertions.assertFalse(allUsers.isEmpty());
        Assertions.assertEquals(allUsers.get(0), TestHelpers.createMockNoAdminUser());
    }

    @Test
    public void getAllUsers_Should_Pass_When_ValidWithoutValidAdminUserPassed() {
        List<User> users = new ArrayList<>();
        users.add(TestHelpers.createMockNoAdminUser());
        UserFilterOptions userFilterOptions = TestHelpers.createUserFilterOptions();

        Mockito.when(userRepository.getAllUsers(userFilterOptions))
                .thenReturn(users);

        List<User> allUsers = userService.getAllUsers(userFilterOptions);

        Mockito.verify(userRepository, Mockito.times(1))
                .getAllUsers(userFilterOptions);
    }

    @Test
    public void getUserByUsername_Should_CallRepository() {
        User userToFind = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserByUsername(Mockito.anyString()))
                .thenReturn(userToFind);

        User result = userService.getUserByUsername(userToFind.getUserName());

        Assertions.assertEquals(userToFind, result);
        Assertions.assertEquals(userToFind.getUserId(), result.getUserId());
        Assertions.assertEquals(userToFind.getUserName(), result.getUserName());
        Assertions.assertEquals(userToFind.getEmail(), result.getEmail());
    }

    @Test
    public void addPhoneNumber_Should_Throw_When_UserIsNotAdmin() {
        String phoneNumber = "088913141414";
        User userTryingToAdd = TestHelpers.createMockNoAdminUser();
        User userToBeUpdated = TestHelpers.createMockNoAdminUser();
        userToBeUpdated.setUserId(777);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.addPhoneNumber(userToBeUpdated, phoneNumber, userTryingToAdd));
    }

    @Test
    public void addPhoneNumber_Should_CallRepository_When_UserIsAdmin() {
        String phoneNumber = "088913141414";
        User userTryingToAdd = TestHelpers.createMockAdminUser();
        User userToBeUpdated = TestHelpers.createMockAdminUser();

        Mockito.when(userRepository.getUserByPhoneNumber(userToBeUpdated.getPhoneNumber()))
                .thenThrow(new EntityNotFoundException("User", "Phone number", userToBeUpdated.getPhoneNumber()));

        userService.addPhoneNumber(userToBeUpdated, phoneNumber, userTryingToAdd);

        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(userToBeUpdated);
    }

    @Test
    public void addPhoneNumber_Should_Throw_When_PhoneNumberExists() {
        User userToBeUpdated = TestHelpers.createMockAdminUser();
        User userToDoUpdates = TestHelpers.createMockAdminUser();
        User userExistingAlready = TestHelpers.createMockNoAdminUser();
        userExistingAlready.setPhoneNumber("0891234567");
        String phoneNumber = "0891234567";

        Mockito.when(userRepository.getUserByPhoneNumber(phoneNumber))
                        .thenReturn(userExistingAlready);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.addPhoneNumber(userToBeUpdated, phoneNumber, userToDoUpdates));
    }

    @Test
    public void deleteUser_Throw_When_UserIsNotAdminAndNotSame() {
        User user = TestHelpers.createMockNoAdminUser();
        User userWhoDeletes = TestHelpers.createMockNoAdminUser();
        userWhoDeletes.setUserId(30);

        Assertions
                .assertThrows(UnauthorizedOperationException.class,
                        () -> userService.deleteUser(user, userWhoDeletes));
    }

    @Test
    public void deleteUser_Should_DeleteUser_When_UserValidParametersPassed() {
        User user = TestHelpers.createMockNoAdminUser();

        userService.deleteUser(user, user);

        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(user);
    }

    @Test
    public void getUserByFirstName_Throw_When_UserIsNotAdmin() {
        User user = TestHelpers.createMockNoAdminUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getUserByFirstName("firstName", user));
    }

    @Test
    public void getUserByFirstName_CallRepository_When_ValidParametersPassed() {
        User user = TestHelpers.createMockAdminUser();

        userService.getUserByFirstName("firstName", user);

        Mockito.verify(userRepository, Mockito.times(1))
                .getUserByFirstName("firstName");
    }

    @Test
    public void getUsersById_Should_ReturnUser_When_MethodCalled() {
        Mockito.when(userRepository.getUserById(2))
                .thenReturn(TestHelpers.createMockNoAdminUser());

        User user = userService.getUserById(2);

        Assertions.assertEquals(2, user.getUserId());
    }

    @Test
    public void deleteAvatar_Should_Throw_When_UserIsNotAdminOrNotSame() {
        User user = TestHelpers.createMockNoAdminUser();
        User userToDoChanges = TestHelpers.createMockNoAdminUser();
        userToDoChanges.setUserId(222);

        Mockito.when(userRepository.getUserById(Mockito.anyInt()))
                .thenReturn(user);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.deleteAvatar(Mockito.anyInt(), userToDoChanges));
    }

    @Test
    public void deleteAvatar_Should_SetDefaultAvatarToUser_When_PassedValidParameters() {
        User userWithChanges = TestHelpers.createMockNoAdminUser();

        Mockito.when(userRepository.getUserById(Mockito.anyInt()))
                .thenReturn(userWithChanges);

        Mockito.when(avatarService.getDefaultAvatar())
                .thenReturn(TestHelpers.createAvatar());

        User userResult = userService.deleteAvatar(Mockito.anyInt(), userWithChanges);

        Assertions.assertEquals(1, userResult.getAvatar().getAvatarId());
    }

    @Test
    public void getAllUsersCount_Should_Pass() {
        Mockito.when(userRepository.getAllUsersCount())
                .thenReturn(1L);

        userService.getAllUsersCount();

        Mockito.verify(userRepository, Mockito.times(1))
                .getAllUsersCount();
    }

    @Test
    public void getUserByEmail_Should_CallRepository() {
        User userToCallMethod = TestHelpers.createMockNoAdminUser();
        String email = "";

        userService.getUserByEmail(email, userToCallMethod);

        Mockito.verify(userRepository, Mockito.times(1))
                .getUserByEmail(email);
    }

    @Test
    public void addRoleToUser_Should_Throw_When_UserIsNotAdmin() {
        User user = TestHelpers.createMockNoAdminUser();
        Role role = TestHelpers.createMockRoleAdmin();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.addRoleToUser(role, user, user));

    }

    @Test
    public void addRoleToUser_Should_CallRepository() {
        User user = TestHelpers.createMockNoAdminUser();
        User admin = TestHelpers.createMockAdminUser();
        Role role = TestHelpers.createMockRoleAdmin();

        userService.addRoleToUser(role, user, admin);

        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(user);
    }

    @Test
    public void removeRoleFromUser_Should_Throw_When_UserIsNotAdmin() {
        User user = TestHelpers.createMockNoAdminUser();
        Role role = TestHelpers.createMockRoleAdmin();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.removeRoleFromUser(role, user, user));

    }

    @Test
    public void removeRoleFromUser_Should_CallRepository() {
        User user = TestHelpers.createMockNoAdminUser();
        User admin = TestHelpers.createMockAdminUser();
        Role role = TestHelpers.createMockRoleAdmin();

        userService.removeRoleFromUser(role, user, admin);

        Mockito.verify(userRepository, Mockito.times(1))
                .updateUser(user);
    }
}
