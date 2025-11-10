package server.repository;

import server.datasource.UserDataSource;
import server.model.User;

import java.util.List;

public class UserRepository {

    private final List<User> users;

    public UserRepository(UserDataSource dataSource) {
        this.users = dataSource.loadUsers();
    }

    public List<User> findAll() {
        return users;
    }

    public User findByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) return null;
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}