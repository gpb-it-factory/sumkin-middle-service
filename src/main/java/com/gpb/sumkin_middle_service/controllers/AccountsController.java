package com.gpb.sumkin_middle_service.controllers;

import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.dto.RegAccountDto;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.AccountService;
import com.gpb.sumkin_middle_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/users")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

    private final UserService userService;
    private final AccountService accountService;

    @PostMapping("/{tgId}/accounts")
    @ActionAudit(value = ActionAudit.ACCOUNT_ACTION)
    public ResponseEntity<AccountDto> createUser(@PathVariable("tgId") Long tgId,
                                                  @RequestBody RegAccountDto regAccountDto) {
        return accountService.registerAccount(tgId, regAccountDto);
    }

    @GetMapping("/{tgId}/accounts")
    @ActionAudit(value = ActionAudit.ACCOUNT_ACTION)
    public ResponseEntity<AccountDto> getUserById(@PathVariable("tgId") Long tgId) {
        return accountService.getAccountsByTgId(tgId);
    }
}
