package com.example.sercuritytest.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // id lich su giao dich

    private String transactionID; // ma giao dich

    private String account; // tai khoan

    private BigDecimal inDebt; // so tien tru

    private BigDecimal have; // so tien nhan

    private Date time; // thoi gian giao dich
}
