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
@Table(name = "deposit")
@Data
public class DepositEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposit_id")
    private Integer depositId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "deposit_Money", nullable = false)
    private String depositMoney;

    @Column(name = "deposit_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ORDERSTATUS depositStatus;
}
