package com.gpb.sumkin_middle_service.controllers;


import com.gpb.sumkin_middle_service.dto.UserDto;
import com.gpb.sumkin_middle_service.dto.GetUserDto;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v2/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    @PostMapping
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

    @Operation(summary = "Find all transfers with pagination")
    @GetMapping("/allusers")
    @ActionAudit(value = ActionAudit.USER_ACTION)
    public List<UserGpb> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        log.info("Finding all transfers with pagination page = " + page + ", " +
                "size = " + size);
        return userService.getAllUsers(page, size);
    }
}

