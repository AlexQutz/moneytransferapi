package com.example.moneytransferapi.controllers;

import com.example.moneytransferapi.dtos.TransactionRequestDTO;
import com.example.moneytransferapi.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping
    public ResponseEntity<String> transferMoney(@RequestBody TransactionRequestDTO request) {
        String response = transactionService.transferMoney(request.getSourceAccountId(), request.getTargetAccountId(), request.getAmount(), request.getCurrency());
        if (response.equals("Transfer successful.")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
