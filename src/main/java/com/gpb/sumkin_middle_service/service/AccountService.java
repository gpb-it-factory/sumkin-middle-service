package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.dto.GetAccountDto;
import com.gpb.sumkin_middle_service.dto.RegAccountDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import com.gpb.sumkin_middle_service.entities.MyError;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.repositories.AccountGpbRepository;
import com.gpb.sumkin_middle_service.repositories.MyErrorRepository;
import com.gpb.sumkin_middle_service.repositories.UserGpbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final UserGpbRepository userRepository;
    private final MyErrorRepository myErrorRepository;
    private final AccountGpbRepository accountGpbRepository;

    public ResponseEntity registerAccount(Long tgId, RegAccountDto regAccountDto) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404);
        } else if (!accountGpbRepository.findByTgId(tgId).isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " уже имеет счет, а он может быть только один";
            log.error(message);
            return getMyErrorResponseEntity(message, 409);
        } else {
            AccountGpb account = AccountGpb.builder()
                    .id(UUID.randomUUID())
                    .userId(userGpb.get().getId())
                    .amount(BigDecimal.valueOf(5000))
                    .accountName(regAccountDto.getAccountName())
                    .build();
            accountGpbRepository.save(account);
            return ResponseEntity
                    .status(201)
                    .body(new AccountDto(account.getId(),
                            account.getAccountName(),
                            account.getAmount()));
        }
    }

    public ResponseEntity getAccountsByTgId(Long tgId) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        List<AccountDto> accountGpb = accountGpbRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404);
        } else if (accountGpb.isEmpty()) {
            String message = "У пользователь c tgId " + tgId + " нет счетов";
            log.error(message);
            return getMyErrorResponseEntity(message, 409);
        } else {
            AccountGpb account = AccountGpb.builder()
                    .id(UUID.randomUUID())
                    .userId(userGpb.get().getId())
                    .accountName(accountGpb.get(0).getAccountName())
                    .amount(accountGpb.get(0).getAmount())
                    .build();
            accountGpbRepository.save(account);
            return ResponseEntity
                    .status(201)
                    .body(new GetAccountDto(account.getId()));
        }
    }

    @ActionAudit(value = ActionAudit.ACCOUNT_ACTION)
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
