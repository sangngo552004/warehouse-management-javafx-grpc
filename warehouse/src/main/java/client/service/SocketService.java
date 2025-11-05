package client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService {
    private static SocketService instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private SocketService() {
    }

    public static synchronized SocketService getInstance() {
        if (instance == null) {
            instance = new SocketService();
        }
        return instance;
    }

    public boolean connect(String ip, int port) {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(ip, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String sendRequest(String request) {
        try {
            out.println(request);
            String reponse = in.readLine();
            return reponse;
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR;" + e.getMessage();
        }
    }

    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance = null;
    }
}
