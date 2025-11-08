package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import server.service.AuthService;
import server.service.HistoryService;
import server.service.ProductService;

import org.slf4j.Logger; // <-- MỚI
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerApp {

    public static final int PORT = 9090;
    private static final Logger log = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {
        log.info("Đang khởi tạo các dịch vụ...");

        AuthService authService = new AuthService();
        ProductService productService = new ProductService();
        HistoryService historyService = new HistoryService();

        WarehouseServiceImpl warehouseImpl = new WarehouseServiceImpl(
                authService,
                productService,
                historyService
        );

        Server server = ServerBuilder.forPort(PORT)
                .addService(warehouseImpl)
                .build();

        try {
            server.start();
            log.info("Server đã khởi động, đang lắng nghe trên cổng: {}", PORT);
            server.awaitTermination();
        } catch (IOException e) {
            log.error("Lỗi I/O khi khởi động server:", e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.error("Server bị gián đoạn:", e);
            e.printStackTrace();
        }
    }
}