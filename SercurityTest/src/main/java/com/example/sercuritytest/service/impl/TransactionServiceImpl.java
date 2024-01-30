package com.example.sercuritytest.service.impl;

import com.example.sercuritytest.dto.TransactionDTO;
import com.example.sercuritytest.entity.TransactionHistory;
import com.example.sercuritytest.repository.TransactionRepository;
import com.example.sercuritytest.service.TransactionService;
import com.example.sercuritytest.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    KeyPair keyPair = CryptoUtils.generateRSAKeyPair(); // gen key thuat toan ma hoa RSA
    SecretKey AesGcm128Key = CryptoUtils.generateAESKey(); // key thuật toán mã hóa AES

    public TransactionServiceImpl() throws Exception {
    }

    /**
     * Dùng để mã hóa dữ liệu
     * @param transactionDTO truyền vào 1 DTO
     * @return trả về 1 DTO đã mã hóa dữ liệu
     * @throws Exception
     */
    @Override
    public TransactionDTO encryptTransactionDTO(TransactionDTO transactionDTO) throws Exception {
        TransactionDTO encryptedDTO = new TransactionDTO();
        encryptedDTO.setTransactionIDOne(CryptoUtils.encryptRSA(transactionDTO.getTransactionIDOne(), keyPair.getPublic()));
        encryptedDTO.setAccountOne(CryptoUtils.encryptRSA(transactionDTO.getAccountOne(), keyPair.getPublic()));
        encryptedDTO.setTransactionIDTrue(CryptoUtils.encryptRSA(transactionDTO.getTransactionIDTrue(), keyPair.getPublic()));
        encryptedDTO.setAccountTrue(CryptoUtils.encryptRSA(transactionDTO.getAccountTrue(), keyPair.getPublic()));
//        encryptedDTO.setInDebt(BigDecimal.valueOf(Long.parseLong(CryptoUtils.encryptRSA(transactionDTO.getInDebt().toString()))));
//        encryptedDTO.setHave(BigDecimal.valueOf(Long.parseLong(CryptoUtils.encryptRSA(transactionDTO.getHave().toString()))));
        encryptedDTO.setInDebt(transactionDTO.getInDebt());
        encryptedDTO.setHave(transactionDTO.getHave());
        return encryptedDTO;
    }

    /**
     * Dùng để thêm 1 bản ghi giao dịch nợ tài khoản nguồn
     * @param transactionDTO truyền vào 1 DTO
     * @return trả về debtTransaction
     * @throws Exception
     */
    @Override
    public TransactionHistory insertTransactionDebit(TransactionDTO transactionDTO) throws Exception {
        //Giải mã trước khi lưu vào db
        String decryptTransactionIDOne = CryptoUtils.decryptRSA(transactionDTO.getTransactionIDOne(), keyPair.getPrivate());
        String decryptAccountOne = CryptoUtils.decryptRSA(transactionDTO.getAccountOne(), keyPair.getPrivate());
//        String decryptInDebt = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getInDebt(), keyPair.getPrivate()));
//        String decryptHave = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getHave(), keyPair.getPrivate()));
//      String decryptTime = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getTime(), keyPair.getPrivate()));

        //Mã hóa AES account
        String encryptAESAccountOne = CryptoUtils.encrypt(decryptAccountOne, AesGcm128Key);

        // Tạo giao dịch nợ cho tài khoản nguồn
        TransactionHistory debtTransaction = new TransactionHistory();
        debtTransaction.setTransactionID(decryptTransactionIDOne);
        debtTransaction.setAccount(encryptAESAccountOne);
        debtTransaction.setInDebt(transactionDTO.getInDebt());
        debtTransaction.setHave(BigDecimal.valueOf(0)); // Không có tiền vào tài khoản nguồn
        debtTransaction.setTime(new Date());

        //Lưu vào DB
        transactionRepository.save(debtTransaction);

        return debtTransaction;
    }

    /**
     * Dùng để tạo một bản ghi giao dịch cho tải khoản đích
     * @param transactionDTO truyền vào 1 DTO
     * @return trả về creditTransaction
     * @throws Exception
     */
    @Override
    public TransactionHistory insertTransactionHave(TransactionDTO transactionDTO) throws Exception {
        //Giải mã trước khi lưu db
        String decryptTransactionIDTrue = CryptoUtils.decryptRSA(transactionDTO.getTransactionIDTrue(), keyPair.getPrivate());
        String decryptAccountTrue = CryptoUtils.decryptRSA(transactionDTO.getAccountTrue(), keyPair.getPrivate());
//        String decryptInDebt = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getInDebt()), keyPair.getPrivate());
//        String decryptHave = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getHave()), keyPair.getPrivate());
//        String decryptTime = CryptoUtils.decryptRSA(String.valueOf(transactionDTO.getTime()), keyPair.getPrivate());

        //Mã hóa AES account
        String encryptAESAccountTrue = CryptoUtils.encrypt(decryptAccountTrue, AesGcm128Key);

        // Tạo giao dịch có cho tài khoản đích
        TransactionHistory creditTransaction = new TransactionHistory();
        creditTransaction.setTransactionID(decryptTransactionIDTrue);
        creditTransaction.setAccount(encryptAESAccountTrue);
        creditTransaction.setInDebt(BigDecimal.valueOf(0)); // Không có nợ cho tài khoản đích
        creditTransaction.setHave(transactionDTO.getHave());
        creditTransaction.setTime(new Date());
        transactionRepository.save(creditTransaction);

        return creditTransaction;
    }
}
