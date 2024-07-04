package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.UserDto;
import com.gpb.sumkin_middle_service.dto.GetUserDto;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.repositories.UserGpbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserGpbRepository userRepository;
    private final AccountService accountService;

    @Transactional
    public ResponseEntity registerUser(UserDto userDto) {
        Long tgId = userDto.getId();
        String tgUsername = userDto.getTgUsername();
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (!isRegistered(tgId)) {
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
            String message = "Пользователь c tgId " + tgId + "уже зарегистрирован";
            log.error(message);
            return accountService.getMyErrorResponseEntity(message, 409, "USER_ALREADY_EXISTS");
        }
    }

    public ResponseEntity getRegisteredUserById(Long tgId) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return accountService.getMyErrorResponseEntity(message, 404, "USER_NOT_FOUND");
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
            return accountService.getMyErrorResponseEntity(message, 404, "USER_NOT_FOUND");
        } else {
            return ResponseEntity
                    .status(200)
                    .body(new GetUserDto(userGpb.get().getId()));
        }
    }

    public Boolean isRegistered(String tgName) {
        Optional<UserGpb> userGpb = userRepository.findByTgUsername(tgName);
        return userGpb.isPresent();
    }

    public Boolean isRegistered(Long tgId) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        return userGpb.isPresent();
    }


    public List<UserGpb> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).getContent();
    }
}
