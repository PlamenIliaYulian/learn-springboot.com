package com.PlamenIliaYulian.Web_Forum.controllers.helpers;

import com.PlamenIliaYulian.Web_Forum.exceptions.AuthenticationException;
import com.PlamenIliaYulian.Web_Forum.exceptions.EntityNotFoundException;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    public static final String LOGGED_USER_ERROR = "No user logged in.";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);

            User user = userService.getUserByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;

        } catch (EntityNotFoundException e) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getPassword(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");

        if (firstSpaceIndex == -1) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpaceIndex + 1);
    }

    private String getUsername(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");

        if (firstSpaceIndex == -1) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpaceIndex);
    }

    public User tryGetUserFromSession(HttpSession httpSession) {
        String currentUser = (String) httpSession.getAttribute("currentUser");

        if(currentUser == null){
            throw new AuthenticationException(LOGGED_USER_ERROR);
        }

        return userService.getUserByUsername(currentUser);
    }
    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getUserByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;

        } catch (EntityNotFoundException e) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
}
