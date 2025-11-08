package server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.model.Transaction;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String HISTORY_FILE = "data/history.json";
    private final List<Transaction> historyDatabase;

    public HistoryService() {
        this.historyDatabase = Collections.synchronizedList(loadHistory());
    }

    private List<Transaction> loadHistory() {
        try (Reader reader = new FileReader(HISTORY_FILE)) {
            Type historyType = new TypeToken<List<Transaction>>() {}.getType();
            List<Transaction> history = gson.fromJson(reader, historyType);
            System.out.println("HistoryService: Đã tải " + history.size() + " giao dịch.");
            return new ArrayList<>(history);
        } catch (Exception e) {
            System.err.println("Lỗi: Không tìm thấy file " + HISTORY_FILE);
            return new ArrayList<>();
        }
    }

    private synchronized void saveHistory() {
        try (Writer writer = new FileWriter(HISTORY_FILE)) {
            gson.toJson(historyDatabase, writer);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file lịch sử: " + e.getMessage());
        }
    }

    public List<Transaction> getHistory() {
        return new ArrayList<>(historyDatabase);
    }

    public synchronized void logTransaction(String clientName, String action, String product, int quantity, String result) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        Transaction transaction = new Transaction(timestamp, clientName, action, product, quantity, result);
        historyDatabase.add(0, transaction);
        saveHistory();
    }
}