package com.example.sercuritytest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionDTO {

    private String transactionIDOne; // ma giao dich thu nhat

    private String transactionIDTrue; // ma giao dich thu hai

    private String accountOne; // tai khoan 1

    private String accountTrue; // tai khoan 2

    private BigDecimal inDebt; // so tien tru

    private BigDecimal have; // tien nhan duoc

//    private Date time;
}
