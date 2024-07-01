package com.gpb.sumkin_middle_service.controllers;


import com.gpb.sumkin_middle_service.dto.UserDto;
import com.gpb.sumkin_middle_service.dto.GetUserDto;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v2/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ActionAudit(value = ActionAudit.USER_ACTION)
    public ResponseEntity<GetUserDto> createUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    @GetMapping("/{tgId}")
    @ActionAudit(value = ActionAudit.USER_ACTION)
    public ResponseEntity<GetUserDto> getUserById(@PathVariable("tgId") Long tgId) {
        return userService.getRegisteredUserById(tgId);
    }


    @GetMapping("/tgName/{tgName}")
    @ActionAudit(value = ActionAudit.USER_ACTION)
    public ResponseEntity<GetUserDto> getUserByTgName(@PathVariable("tgName") String tgName) {
        return userService.getRegisteredUserByTgName(tgName);
    }
}

