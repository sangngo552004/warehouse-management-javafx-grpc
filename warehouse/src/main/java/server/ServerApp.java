package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerApp {

    public static final int PORT = 9090;

    public static void main(String[] args) {
        System.out.println("Khởi động Warehouse Server...");

        DataService dataService = new DataService();

        Server server = ServerBuilder.forPort(PORT)
                .addService(new WarehouseServiceImpl(dataService))
                .build();

        try {
            server.start();
            System.out.println("Server đã khởi động thành công trên cổng: " + PORT);
            server.awaitTermination();
        } catch (IOException e) {
            System.err.println("Lỗi I/O khi khởi động server: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Server bị gián đoạn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}