package server.repository;

import server.datasource.TransactionDataSource;
import server.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionRepository {

    private final List<Transaction> transactions;
    private final TransactionDataSource dataSource;

    public TransactionRepository(TransactionDataSource dataSource) {
        this.dataSource = dataSource;
        this.transactions = Collections.synchronizedList(dataSource.loadTransactions());
    }

    public List<Transaction> findAll() {
        synchronized (transactions) {
            return new ArrayList<>(transactions);
        }
    }

    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(0, transaction); // thêm vào đầu
        dataSource.saveTransactions(transactions);
    }
}
