package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.exceptions.UnauthorizedOperationException;
import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserAdministrativeDto;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserDto;
import com.PlamenIliaYulian.Web_Forum.models.UserFilterOptions;
import com.PlamenIliaYulian.Web_Forum.models.dtos.UserDtoUpdate;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelsMapper modelsMapper;

    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }

    /*Plamen*/
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(
            tags = {"User"}
    )
    public User createUser(@RequestBody @Valid UserDto userDto) {
        User user = modelsMapper.userFromDto(userDto);
        return userService.createUser(user);
    }

    /*Ilia*/
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{username}")
    void deleteUser(@RequestHeader HttpHeaders headers, @PathVariable String username) {
        try {
            User userIsAuthenticated = authenticationHelper.tryGetUser(headers);
            User userToBeDeleted = userService.getUserByUsername(username);
            userService.deleteUser(userIsAuthenticated, userToBeDeleted);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    /*Yuli - implemented:*/
    @PutMapping("/{username}")
    @Operation(
            summary = "Updates the information of the user found by the provided username.",
            description = "Used to update User's first name, last name, email or password.",
            parameters = {
                    @Parameter(name = "username",
                            description = "Path variable.",
                            example = "emily_jackson"),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = User.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Username and password provided in the 'Authorization' header do not match any user in the database",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Invalid authentication.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Username trying to execute the request must be either admin OR the same user to which the profile belongs.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "Unauthorized operation.")
                                    },
                                            mediaType = "plain text")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "There is no user with this 'username'.",
                            content = {
                                    @Content(examples = {
                                            @ExampleObject(value = "User with username 'username' not found.")
                                    },
                                            mediaType = "plain text")
                            }
                    )
            })
    @SecurityRequirement(name = "BasicAuth")
    User updateUser(@RequestHeader HttpHeaders headers,
                    @PathVariable String username,
                    @Valid @RequestBody UserDtoUpdate userDtoUpdate) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromDtoUpdate(userDtoUpdate, username);
            return userService.updateUser(userToBeUpdated, userToDoUpdates);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/administrative-changes/{username}")
    public User updateAdministrativeChanges(@PathVariable String username,
                                            @RequestHeader HttpHeaders headers,
                                            @Valid @RequestBody UserAdministrativeDto userAdministrativeDto) {
        try {
            User userToDoUpdates = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = modelsMapper.userFromAdministrativeDto(userAdministrativeDto, username);
            return userService.makeAdministrativeChanges(userToDoUpdates, userToBeUpdated);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /*Ilia*/
    /*We have to delete this method!*/
    /*TODO It is not the best way for this method.*/
    @GetMapping("/first-name/{firstName}")
    public User getUserByFirstName(@PathVariable String firstName,
                                   @RequestHeader HttpHeaders headers) {
        try {
            User userToBeAuthorized = authenticationHelper.tryGetUser(headers);
            return userService.getUserByFirstName(firstName, userToBeAuthorized);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    e.getMessage());
        }
    }

    /*TODO It is not the best way for this method.*/
    /*TODO to implement authentication required for this end point.*/
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        try {
            return userService.getUserByUsername(username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    /*Plamen*/
    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        try {
            User userExecutingTheRequest = authenticationHelper.tryGetUser(headers);
            UserFilterOptions userFilterOptions = new UserFilterOptions(username, email, firstName, sortBy, sortOrder);
            return userService.getAllUsers(userExecutingTheRequest, userFilterOptions);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /*TODO It is not the best way for this method.*/
    /*Ilia*/
    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable int id) {
        try {
            return userService.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @PutMapping("/{userToBeUpdated}/avatar")
    public User addAvatar(@PathVariable int userToBeUpdated, @RequestBody byte[] avatar, @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            return userService.addAvatar(userToBeUpdated, avatar, userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    /*Plamen*/
    /*We have to find a better way to set the phone number.*/
    @PutMapping("/{username}/PhoneNumber")
    public User addPhoneNumber(@PathVariable String username,
                               @RequestBody String phoneNumber,
                               @RequestHeader HttpHeaders headers) {
        try {
            User userToDoChanges = authenticationHelper.tryGetUser(headers);
            User userToBeUpdated = userService.getUserByUsername(username);
            return userService.addPhoneNumber(userToBeUpdated, phoneNumber, userToDoChanges);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
