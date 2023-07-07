package com.example.testproject.controller;

import com.example.testproject.dto.AccountDto;
import com.example.testproject.dto.AccountReqDto;
import com.example.testproject.dto.DepositWithdrawReqDto;
import com.example.testproject.dto.SendMoneyDto;
import com.example.testproject.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankTransaction")
@Slf4j
public class BankController {
    @Autowired
    BankService service;

    @PostMapping("createUser")
    public ResponseEntity<Boolean> createUser(@RequestBody String userName) {
        return ResponseEntity.ok(service.createUser(userName));
    }

    @PostMapping("createAccount")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountReqDto reqDto) {
        return ResponseEntity.ok(service.createAccount(reqDto));
    }

    @PostMapping("withdraw")
    public ResponseEntity<Boolean> withdraw(@RequestBody DepositWithdrawReqDto reqDto) {
        return ResponseEntity.ok(service.withdraw(reqDto));
    }

    @PostMapping("deposit")
    public ResponseEntity<Boolean> deposit(@RequestBody DepositWithdrawReqDto reqDto) {
        return ResponseEntity.ok(service.deposit(reqDto));
    }

    @PostMapping("sendMoney")
    public ResponseEntity<Boolean> sendMoney(@RequestBody SendMoneyDto sendMoneyDto) {
        return ResponseEntity.ok(service.sendMoney(sendMoneyDto));
    }
}
