package server.datasource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.model.Transaction;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionDataSource {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String TRANSACTION_FILE = "data/history.json";

    public List<Transaction> loadTransactions() {
        try (Reader reader = new FileReader(TRANSACTION_FILE)) {
            Type type = new TypeToken<List<Transaction>>() {}.getType();
            List<Transaction> transactions = gson.fromJson(reader, type);
            System.out.println("TransactionDataSource: Đã tải " + (transactions != null ? transactions.size() : 0) + " giao dịch.");
            return transactions != null ? transactions : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Không tìm thấy file " + TRANSACTION_FILE + ", trả về danh sách rỗng.");
            return new ArrayList<>();
        }
    }

    public void saveTransactions(List<Transaction> transactions) {
        try (Writer writer = new FileWriter(TRANSACTION_FILE)) {
            gson.toJson(transactions, writer);
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu file giao dịch: " + e.getMessage());
        }
    }
}