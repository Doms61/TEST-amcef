package com.dominik.intro.project.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dominik.intro.project.connector.ExternalConnector;

import lombok.RequiredArgsConstructor;

/**
 * User service class for handling logic connected to the user
 */
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private ExternalConnector connector;

    /**
     * checks if the user has access
     * @param userId userId
     * @return True, if a user is present with given userId
     */
    public boolean userHasAccess(int userId) {
        var userDtoStream = connector.getUsers().stream().filter(userDto -> userDto.getId() == userId).collect(Collectors.toList());
        return !userDtoStream.isEmpty();
    }
}
