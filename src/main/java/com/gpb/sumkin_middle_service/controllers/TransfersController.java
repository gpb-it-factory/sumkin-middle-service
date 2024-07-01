package com.gpb.sumkin_middle_service.controllers;

import com.gpb.sumkin_middle_service.dto.RegisterTransferDto;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransfersController {

    private final TransferService transferService;

    @PostMapping
    @ActionAudit(value = ActionAudit.TRANSFER_ACTION)
    public ResponseEntity transfer(@Valid @RequestBody RegisterTransferDto registerTransferDto) {
        return transferService.transfer(registerTransferDto);
    }


}
