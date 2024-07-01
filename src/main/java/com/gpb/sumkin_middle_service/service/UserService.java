package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.UserDto;
import com.gpb.sumkin_middle_service.dto.GetUserDto;
import com.gpb.sumkin_middle_service.entities.MyError;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.repositories.MyErrorRepository;
import com.gpb.sumkin_middle_service.repositories.UserGpbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserGpbRepository userRepository;
    private final MyErrorRepository myErrorRepository;

    public ResponseEntity registerUser(UserDto userDto) {
        Long tgId = userDto.getId();
        String tgUsername = userDto.getTgUsername();
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            UserGpb user = UserGpb.builder()
                    .id(UUID.randomUUID())
                    .tgId(tgId)
                    .tgUsername(tgUsername)
                    .build();
            userRepository.save(user);
            return ResponseEntity
                    .status(201)
                    .body(new GetUserDto(user.getId()));
        } else {
            String message = "Пользователь c tgId" + tgId + "уже зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 409);
        }
    }

    public ResponseEntity getRegisteredUserById(Long tgId) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404);
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new GetUserDto(userGpb.get().getId()));
        }
    }

    public ResponseEntity getRegisteredUserByTgName(String tgName) {
        Optional<UserGpb> userGpb = userRepository.findByTgUsername(tgName);
        log.info("Пользователь c tgName {} зарегистрирован", tgName);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgName " + tgName + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404);
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new GetUserDto(userGpb.get().getId()));
        }
    }

    private ResponseEntity<MyError> getMyErrorResponseEntity(String message, int code) {
        MyError error = MyError.builder()
                .message(message)
                .type("USER_NOT_FOUND")
                .code(code)
                .traceId(UUID.randomUUID())
                .build();
        myErrorRepository.save(error);
        return ResponseEntity
                .status(code)
                .body(error);
    }
}
