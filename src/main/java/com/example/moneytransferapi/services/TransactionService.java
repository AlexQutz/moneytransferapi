package com.example.moneytransferapi.services;

import com.example.moneytransferapi.models.Account;
import com.example.moneytransferapi.models.Transaction;
import com.example.moneytransferapi.repositories.AccountRepository;
import com.example.moneytransferapi.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String transferMoney(Long sourceAccountId, Long targetAccountId, Double amount, String currency) {

        if (sourceAccountId.equals(targetAccountId)) {
            return "Transfer between the same account is not allowed.";
        }

        Optional<Account> sourceAccountOpt = accountRepository.findById(sourceAccountId);
        Optional<Account> targetAccountOpt = accountRepository.findById(targetAccountId);

        if (!sourceAccountOpt.isPresent() || !targetAccountOpt.isPresent()) {
            return "One or both accounts do not exist.";
        }

        Account sourceAccount = sourceAccountOpt.get();
        Account targetAccount = targetAccountOpt.get();

        if (sourceAccount.getBalance() < amount) {
            return "Insufficient balance to process the transfer.";
        }

        updateBalances(amount, sourceAccount, targetAccount);

        saveTransaction(amount, currency, sourceAccount, targetAccount);

        return "Transfer successful.";
    }

    private void saveTransaction(Double amount, String currency, Account sourceAccount, Account targetAccount) {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    private void updateBalances(Double amount, Account sourceAccount, Account targetAccount) {
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
