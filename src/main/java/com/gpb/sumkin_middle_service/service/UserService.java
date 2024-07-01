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
        Optional<UserGpb> userGpb = userRepository.getByTgId(tgId);
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
}
