package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDto {
    private Integer accountId;
    private String username;
    private String accountNumber;
    private String accountMoney;
    private String crteTmsp;
}
