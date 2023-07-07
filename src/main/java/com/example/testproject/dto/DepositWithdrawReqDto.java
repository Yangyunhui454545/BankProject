package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DepositWithdrawReqDto {
    private Integer userId;
    private String accountNumber; //출금계좌
    private String accountPssWord;
    private String money;
}
