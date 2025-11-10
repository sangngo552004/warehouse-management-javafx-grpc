package server.service;

import server.model.Transaction;
import server.repository.TransactionRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getHistory() {
        return transactionRepository.findAll();
    }

    public void logTransaction(String clientName, String action, String product, int quantity, String result) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        Transaction transaction = new Transaction(timestamp, clientName, action, product, quantity, result);
        transactionRepository.addTransaction(transaction);
    }
}
