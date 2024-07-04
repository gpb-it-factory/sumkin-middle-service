package com.gpb.sumkin_middle_service.controllers;

import com.gpb.sumkin_middle_service.dto.RegisterTransferDto;
import com.gpb.sumkin_middle_service.entities.Transfer;
import com.gpb.sumkin_middle_service.logging.ActionAudit;
import com.gpb.sumkin_middle_service.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Find all transfers with pagination")
    @GetMapping("/alltransfers")
    public List<Transfer> findAllTransfers(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size) {
        log.info("Finding all transfers with pagination page = " + page + ", " +
                "size = " + size);
        return transferService.getAllTransfers(page, size);
    }


}
