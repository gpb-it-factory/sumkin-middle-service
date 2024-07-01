package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.UserDto;
import com.gpb.sumkin_middle_service.dto.GetUserDto;
import com.gpb.sumkin_middle_service.entities.MyError;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
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

    @ActionAudit(value = ActionAudit.USER_ACTION)
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
            log.error("Пользователь c tgId {} уже существует", tgId);
            MyError error = MyError.builder()
                    .message("Пользователь c tgId " + tgId + " уже существует")
                    .type("USER_ALREADY_EXISTS")
                    .code(409)
                    .traceId(UUID.randomUUID())
                    .build();
            myErrorRepository.save(error);
            return ResponseEntity
                    .status(409)
                    .body(error);
        }
    }

    public ResponseEntity getRegisteredUserById(Long tgId) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            log.error("Пользователь c tgId {} не существует", tgId);
            MyError error = MyError.builder()
                    .message("Пользователь c tgId " + tgId + " не существует")
                    .type("USER_NOT_FOUND")
                    .code(404)
                    .traceId(UUID.randomUUID())
                    .build();
            myErrorRepository.save(error);
            return ResponseEntity
                    .status(404)
                    .body(error);
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new GetUserDto(userGpb.get().getId()));
        }
    }

    public ResponseEntity getRegisteredUserByTgName(String tgName) {
        Optional<UserGpb> userGpb = userRepository.findByTgUsername(tgName);
        log.info("Пользователь c tgName {} существует", tgName);
        if (userGpb.isEmpty()) {
            log.error("Пользователь c tgName {} не существует", tgName);
            MyError error = MyError.builder()
                    .message("Пользователь c tgName " + tgName + " не существует")
                    .type("USER_NOT_FOUND")
                    .code(404)
                    .traceId(UUID.randomUUID())
                    .build();
            myErrorRepository.save(error);
            return ResponseEntity
                    .status(404)
                    .body(error);
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new GetUserDto(userGpb.get().getId()));
        }
    }
}
