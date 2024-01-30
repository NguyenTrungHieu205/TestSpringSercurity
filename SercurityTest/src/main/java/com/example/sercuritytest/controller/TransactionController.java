package com.example.sercuritytest.controller;

import com.example.sercuritytest.dto.TransactionDTO;
import com.example.sercuritytest.entity.TransactionHistory;
import com.example.sercuritytest.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * API lưu thông tin lịch sử giao dịch
     * @param transactionDTO truyền vào 1 DTO
     * @return
     * @throws Exception
     */
    @PostMapping("/create")
    public ResponseEntity<TransactionHistory> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception {
            // Mã hóa các thông tin trong TransactionDTO trước khi gửi đi
            TransactionDTO encryptedTransactionDTO = transactionService.encryptTransactionDTO(transactionDTO);
            // Gọi service với TransactionDTO đã được mã hóa
            transactionService.insertTransactionDebit(encryptedTransactionDTO);
            transactionService.insertTransactionHave(encryptedTransactionDTO);
            // Trả về phản hồi thành công
            return ResponseEntity.ok().build();
    }
}
