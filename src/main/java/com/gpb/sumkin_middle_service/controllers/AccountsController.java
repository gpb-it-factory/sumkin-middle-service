package com.gpb.sumkin_middle_service.controllers;

import com.gpb.sumkin_middle_service.dto.AccountDto;
import com.gpb.sumkin_middle_service.dto.RegAccountDto;
import com.gpb.sumkin_middle_service.entities.AccountGpb;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.AccountService;
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
public class AccountsController {

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

    @Operation(summary = "Find all accounts with pagination")
    @GetMapping("/allaccounts")
    @ActionAudit(value = ActionAudit.ACCOUNT_ACTION)
    public List<AccountGpb> findAllAccounts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        log.info("Finding all accounts with pagination page = " + page + ", " +
                "size = " + size);
        return accountService.getAllAccounts(page, size);
    }
}
