package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Dịch vụ này tải và quản lý dữ liệu người dùng từ file users.json.
 */
public class DataService {

    private List<User> userDatabase;

    public DataService() {
        try {
            loadUsers();
        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file users.json!");
            this.userDatabase = List.of();
        }
    }

    private void loadUsers() throws FileNotFoundException {
        Gson gson = new Gson();
        Reader reader = new FileReader("data/users.json");

        Type userListType = new TypeToken<Map<String, List<User>>>() {}.getType();
        Map<String, List<User>> userMap = gson.fromJson(reader, userListType);

        this.userDatabase = userMap.get("users");
        System.out.println("Đã tải thành công " + this.userDatabase.size() + " người dùng.");
    }

    public Optional<User> validateUser(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        for (User user : userDatabase) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}