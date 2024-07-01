package com.gpb.sumkin_middle_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserDto {

    private final Long id;
    private final String tgUsername;
}
