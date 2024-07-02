package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.dto.RegAccountDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import com.gpb.sumkin_middle_service.entities.MyError;
import com.gpb.sumkin_middle_service.entities.Transfer;
import com.gpb.sumkin_middle_service.entities.UserGpb;
import com.gpb.sumkin_middle_service.repositories.AccountGpbRepository;
import com.gpb.sumkin_middle_service.repositories.MyErrorRepository;
import com.gpb.sumkin_middle_service.repositories.TransferRepository;
import com.gpb.sumkin_middle_service.repositories.UserGpbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final UserGpbRepository userRepository;
    private final MyErrorRepository myErrorRepository;
    private final AccountGpbRepository accountGpbRepository;
    private final TransferRepository transferRepository;


    @Transactional
    public ResponseEntity registerAccount(Long tgId, RegAccountDto regAccountDto) {
        Optional<UserGpb> userGpb = userRepository.findByTgId(tgId);
        Optional<AccountGpb> accountGpb = accountGpbRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404, "USER_NOT_FOUND");
        } else if (accountGpb.isPresent()) {
            String message = "Пользователь c tgId " + tgId + " уже имеет счет, а он может быть только один";
            log.error(message);
            return getMyErrorResponseEntity(message, 409, "ACCOUNT_ALREADY_EXISTS");
        } else {
            AccountGpb account = AccountGpb.builder()
                    .id(UUID.randomUUID())
                    .userId(userGpb.get().getId())
                    .tgId(tgId)
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
        Optional<AccountGpb> accountGpb = accountGpbRepository.findByTgId(tgId);
        if (userGpb.isEmpty()) {
            String message = "Пользователь c tgId " + tgId + " не зарегистрирован";
            log.error(message);
            return getMyErrorResponseEntity(message, 404, "USER_NOT_FOUND");
        } else if (accountGpb.isEmpty()) {
            String message = "У пользователя c tgId " + tgId + " нет счетов";
            log.error(message);
            return getMyErrorResponseEntity(message, 409, "ACCOUNT_NOT_FOUND");
        } else {
            return ResponseEntity
                    .status(200)
                    .body(accountGpb.get());
        }
    }

    public ResponseEntity<MyError> getMyErrorResponseEntity(String message, int code,
    String type) {
        MyError error = MyError.builder()
                .message(message)
                .type(type)
                .code(code)
                .traceId(UUID.randomUUID())
                .build();
        myErrorRepository.save(error);
        return ResponseEntity
                .status(code)
                .body(error);
    }

    public Boolean hasAccount(String username) {
        return !accountGpbRepository.findByTgUsername(username).isEmpty();
    }

    public BigDecimal getBalance(String username) {
        return accountGpbRepository.findByTgUsername(username).get(0).getAmount();
    }

    @Transactional
    public UUID performTransfer(AccountGpb from, AccountGpb to, BigDecimal amount) {
        from.setAmount(from.getAmount().subtract(amount));
        to.setAmount(to.getAmount().add(amount));
        accountGpbRepository.save(from);
        accountGpbRepository.save(to);
        Transfer transfer = Transfer.builder()
                .id(UUID.randomUUID())
                .fromAccount(from.getId())
                .toAccount(to.getId())
                .amount(amount)
                .build();
        transferRepository.save(transfer);
        return transfer.getId();
    }

    public AccountGpb getAccountByUsername(String username) {
        return accountGpbRepository.findByUsername(username);
    }
}
