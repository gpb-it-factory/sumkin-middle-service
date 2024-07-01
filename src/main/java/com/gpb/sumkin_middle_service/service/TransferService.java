package com.gpb.sumkin_middle_service.service;

import com.gpb.sumkin_middle_service.dto.RegisterTransferDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import com.gpb.sumkin_middle_service.entities.Transfer;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferService {
    private final UserService userService;
    private final AccountService accountService;

    public ResponseEntity transfer(RegisterTransferDto registerTransferDto) {
        String from = registerTransferDto.getFrom();
        String to = registerTransferDto.getTo();

        if (userService.isRegistered(from) &&
                userService.isRegistered(to) &&
                accountService.hasAccount(from) &&
                accountService.hasAccount(to)) {
            if (transferPossible(registerTransferDto)) {
                AccountGpb fromAccount = accountService.getAccountByUsername(from);
                AccountGpb toAccount = accountService.getAccountByUsername(to);
                BigDecimal amount = registerTransferDto.getAmount();
                UUID transferId = accountService.performTransfer(fromAccount, toAccount, amount);
                return ResponseEntity
                        .status(201)
                        .body(transferId);
            } else {
                String message = "Недостаточно средств";
                log.error(message);
                return accountService.getMyErrorResponseEntity(message, 400, "INSUFFICIENT_FUNDS");
            }
        } else {
            String message = "Все пользователи должны быть зарегистрированы и иметь счет";
            log.error(message);
            return accountService.getMyErrorResponseEntity(message, 404, "USER_NOT_FOUND");
        }    }


    private boolean transferPossible(RegisterTransferDto registerTransferDto) {
        return accountService.getBalance(registerTransferDto.getFrom())
                .compareTo(registerTransferDto.getAmount()) >= 0;
    }
}
