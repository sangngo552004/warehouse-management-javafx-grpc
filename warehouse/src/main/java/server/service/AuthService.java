package server.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthService {

    private List<User> userDatabase;
    private final String USERS_FILE = "data/users.json";
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public AuthService() {
        try {
            loadUsers();
        } catch (FileNotFoundException e) {
            log.error("Lỗi: Không tìm thấy file {}. Tạo danh sách rỗng.", USERS_FILE, e);
            this.userDatabase = List.of();
        }
    }

    private void loadUsers() throws FileNotFoundException {
        Gson gson = new Gson();
        Reader reader = new FileReader(USERS_FILE);

        Type userListType = new TypeToken<Map<String, List<User>>>() {}.getType();
        Map<String, List<User>> userMap = gson.fromJson(reader, userListType);

        this.userDatabase = userMap.get("users");
        log.info("AuthService: Đã tải {} người dùng.", this.userDatabase.size());
    }

    public Optional<User> validateUser(String username, String password) {
        if (username == null || password == null) {

            return Optional.empty();
        }
        return userDatabase.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }
}