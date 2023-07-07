package com.example.testproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "orders")
@Entity
@Builder
@DynamicInsert
public class OrdrEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "order_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private ORDERCODE ordercode;

    @Column(name = "crte_tmsp")
    private LocalDateTime crteTmsp;

    @PrePersist
    public void preprsist() {
        this.crteTmsp = this.crteTmsp == null ? LocalDateTime.now() : this.crteTmsp;
    }
}

