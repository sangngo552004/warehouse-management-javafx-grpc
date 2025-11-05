package client.service;

public class SessionManager {
    private static String username;
    private static String role;

    public static void createSession(String user, String userRole) {
        username = user;
        role = userRole;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isManager() {
        return "Manager".equals(role);
    }
    
    public static void clearSession() {
        username = null;
        role = null;
    }
}
