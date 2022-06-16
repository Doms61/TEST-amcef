package com.dominik.intro.project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class UsersList {

    private List<UserDto> UserDtos;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public UsersList(List<UserDto> UserDtos) {
        this.UserDtos = UserDtos;
    }
}
