package com.example.testproject.service;

import com.example.testproject.dto.AccountDto;
import com.example.testproject.dto.AccountReqDto;
import com.example.testproject.dto.DepositWithdrawReqDto;
import com.example.testproject.dto.SendMoneyDto;
import com.example.testproject.entity.*;
import com.example.testproject.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BankService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WithdrawRepository withdrawRepository;
    @Autowired
    DepositRepository depositRepository;

    ModelMapper mapper = new ModelMapper();

    public AccountDto createAccount(AccountReqDto reqDto) {
        AccountEntity accountEntity = AccountEntity.builder()
                .userId(reqDto.getUserId())
                .accountPssWord(reqDto.getAccountPssword())
                .accountNumber(reqDto.getAccountNumber())
                .build();

        accountEntity = accountRepository.save(accountEntity);
        AccountDto accountDto = mapper.map(accountRepository.findById(accountEntity.getAccountId()), AccountDto.class);
        accountDto.setUsername(userRepository.findById(reqDto.getUserId()).get().getUserName());
        return accountDto;
    }

    public boolean createUser(String userName) {
        try {
            UserEntity userEntity = UserEntity.builder()
                    .userName(userName)
                    .build();
            userRepository.save(userEntity);
        } catch (Exception e) {
            log.error("FAIL CREATE USER {}", e);
            return false;
        }
        return true;
    }

    public boolean withdraw(DepositWithdrawReqDto reqDto) {
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(reqDto.getAccountNumber());
            // 유효성 검사
            checkUserAccount(accountEntity, reqDto.getUserId(), reqDto.getAccountPssWord(), reqDto.getMoney());
            // 계좌 업데이트
            accountEntity.setAccountMoney(minusMoney(accountEntity.getAccountMoney(), reqDto.getMoney()));
            accountRepository.save(accountEntity);
            // 주문내역 저장
            OrdrEntity ordrEntity = orderEntity(ORDERCODE.WITHDRAW, reqDto.getUserId());
            // 출금내역 저장
            withdrawEntity(reqDto.getAccountNumber(), ordrEntity.getOrderId(), ORDERSTATUS.SUCCESS, reqDto.getMoney());
        } catch (Exception e) {
            log.error("WITHDRAW FAIL {}", e);
        }
        return true;
    }

    public boolean deposit(DepositWithdrawReqDto reqDto) {
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(reqDto.getAccountNumber());
            // 유효성 검사
            if (!accountEntity.getUserId().equals(reqDto.getUserId())) {
                throw new RuntimeException("계좌 소유자가 다릅니다.");
            }
            if (!accountEntity.getAccountPssWord().equals(reqDto.getAccountPssWord())) {
                throw new RuntimeException("계좌 비밀번호가 틀립니다.");
            }
            // 계좌 업데이트
            accountEntity.setAccountMoney(plusMoney(accountEntity.getAccountMoney(), reqDto.getMoney()));
            accountRepository.save(accountEntity);
            // 주문내역 저장
            OrdrEntity ordrEntity = orderEntity(ORDERCODE.DEPOSIT, reqDto.getUserId());
            // 입금 내역 저장
            depositEntity(ordrEntity.getOrderId(), reqDto.getMoney(), reqDto.getAccountNumber(), ORDERSTATUS.SUCCESS);
        } catch (Exception e) {
            log.error("DEPOSIT FAIL {}", e);
        }
        return true;
    }

    public boolean sendMoney(SendMoneyDto sendMoneyDto) {
        try {
            AccountEntity accountEntity = accountRepository.findByAccountNumber(sendMoneyDto.getAccountNumber());
            AccountEntity sendAccount = accountRepository.findByAccountNumber(sendMoneyDto.getSendAccountNumber());
            if (!sendAccount.getUserId().equals(sendMoneyDto.getSendUserId())) {
                throw new RuntimeException("보내시는 계좌 소유주를 확인하세요.");
            }
            // 유효성 검사
            checkUserAccount(accountEntity, sendMoneyDto.getUserId(), sendMoneyDto.getAccountPssWord(), sendMoneyDto.getSendMoney());

            // 송금
            accountEntity.setAccountMoney(minusMoney(accountEntity.getAccountMoney(), sendMoneyDto.getSendMoney()));
            accountRepository.save(accountEntity);

            // 출금
            sendAccount.setAccountMoney(plusMoney(sendAccount.getAccountMoney(), sendMoneyDto.getSendMoney()));
            accountRepository.save(sendAccount);

            // 주문내역 저장
            OrdrEntity ordrEntity = orderEntity(ORDERCODE.SENDMONEY, sendMoneyDto.getUserId());

            // 송금내역 저장
            depositEntity(ordrEntity.getOrderId(), sendMoneyDto.getSendMoney(), sendMoneyDto.getSendAccountNumber(), ORDERSTATUS.SUCCESS);

            // 출금내역 저장
            withdrawEntity(sendMoneyDto.getAccountNumber(), ordrEntity.getOrderId(), ORDERSTATUS.SUCCESS, sendMoneyDto.getSendMoney());
        } catch (Exception e) {
            throw new RuntimeException("FAIL SENDING MONEY {}", e);
        }
        return true;
    }

    public String minusMoney(String originMoney, String minusMoney) {
        return String.valueOf(Integer.parseInt(originMoney) - Integer.parseInt(minusMoney));
    }

    public String plusMoney(String originMoney, String plusMoney) {
        return String.valueOf(Integer.parseInt(originMoney) + Integer.parseInt(plusMoney));
    }

    public void withdrawEntity(String accountNumber, Integer orderId, ORDERSTATUS orderstatus, String money) {
        WithdrawEntity withdraw = WithdrawEntity.builder()
                .accountNumber(accountNumber)
                .orderId(orderId)
                .withdrawStatus(orderstatus)
                .withdrawMoney(money)
                .build();
        withdrawRepository.save(withdraw);

    }

    public void depositEntity(Integer orderId, String money, String accountNumber, ORDERSTATUS orderstatus) {
        DepositEntity depositEntity = DepositEntity.builder()
                .orderId(orderId)
                .depositMoney(money)
                .accountNumber(accountNumber)
                .depositStatus(orderstatus)
                .build();
        depositRepository.save(depositEntity);
    }

    public OrdrEntity orderEntity(ORDERCODE ordercode, Integer userId) {
        OrdrEntity ordrEntity = OrdrEntity.builder()
                .ordercode(ordercode)
                .userId(userId)
                .build();

        return orderRepository.save(ordrEntity);
    }

    public void checkUserAccount(AccountEntity accountEntity, Integer userId, String pssword, String money) {
        if (!accountEntity.getUserId().equals(userId)) {
            throw new RuntimeException("계좌 소유자가 다릅니다.");
        }
        if (!accountEntity.getAccountPssWord().equals(pssword)) {
            throw new RuntimeException("계좌 비밀번호가 틀립니다.");
        }
        if (Integer.parseInt(money) > Integer.parseInt(accountEntity.getAccountMoney())) {
            throw new RuntimeException("잔액 부족입니다.");
        }
    }
}
