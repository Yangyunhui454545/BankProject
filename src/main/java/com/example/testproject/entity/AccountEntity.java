package com.example.testproject.entity;

import com.example.testproject.config.DataCryptoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@DynamicInsert
@DynamicUpdate
@Entity
@Data
@Builder
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Convert(converter = DataCryptoConverter.class)
    @Column(name = "account_pssWord", nullable = false)
    private String accountPssWord;

    @Column(name = "account_money", nullable = false)
    private String accountMoney;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "crte_tmsp")
    private LocalDateTime crteTmsp;

    @PrePersist
    public void preprsist() {
        this.accountMoney = StringUtils.isBlank(this.accountMoney) ? "0" : this.accountMoney;
        this.crteTmsp = this.crteTmsp == null ? LocalDateTime.now() : this.crteTmsp;
    }

}
