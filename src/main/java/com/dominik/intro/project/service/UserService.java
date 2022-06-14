package com.dominik.intro.project.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dominik.intro.project.connector.ExternalConnector;
import com.dominik.intro.project.model.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private ExternalConnector connector;

    public boolean userHasAccess(int userId) {
        var userDtoStream = connector.getUsers().stream().filter(userDto -> userDto.getId() == userId).collect(Collectors.toList());
        return !userDtoStream.isEmpty();
    }
}
