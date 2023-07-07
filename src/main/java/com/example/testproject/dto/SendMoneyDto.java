package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SendMoneyDto {
    private Integer userId;
    private String accountNumber;
    private String accountPssWord;
    private String sendMoney;
    private String sendAccountNumber;
    private Integer sendUserId;
}
