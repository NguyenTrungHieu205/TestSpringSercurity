package com.example.sercuritytest.service;

import com.example.sercuritytest.dto.TransactionDTO;
import com.example.sercuritytest.entity.TransactionHistory;

public interface TransactionService {
    TransactionDTO encryptTransactionDTO(TransactionDTO transactionDTO) throws Exception;
    TransactionHistory insertTransactionDebit(TransactionDTO transactionDTO) throws Exception;

    TransactionHistory insertTransactionHave(TransactionDTO transactionDTO) throws Exception;
}
