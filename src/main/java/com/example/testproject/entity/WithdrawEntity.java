package com.example.testproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name = "withdraw")
@Data
public class WithdrawEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdraw_id")
    private Integer withdrawId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "withdraw_money", nullable = false)
    private String withdrawMoney;

    @Column(name = "withdraw_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ORDERSTATUS withdrawStatus;



}
