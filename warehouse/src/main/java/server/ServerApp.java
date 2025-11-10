package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import server.datasource.ProductDataSource;
import server.datasource.TransactionDataSource;
import server.datasource.UserDataSource;
import server.repository.ProductRepository;
import server.repository.TransactionRepository;
import server.repository.UserRepository;
import server.service.AuthService;
import server.service.ProductService;

import org.slf4j.Logger; // <-- MỚI
import org.slf4j.LoggerFactory;
import server.service.TransactionService;

import java.io.IOException;

public class ServerApp {

    public static final int PORT = 9090;
    private static final Logger log = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {
        log.info("Đang khởi tạo các dịch vụ...");

        UserDataSource dataSource = new UserDataSource();
        UserRepository userRepository = new UserRepository(dataSource);
        AuthService authService = new AuthService(userRepository);

        ProductDataSource productDataSource = new ProductDataSource();
        ProductRepository productRepository = new ProductRepository(productDataSource);
        ProductService productService = new ProductService(productRepository);


        TransactionDataSource   transactionDataSource = new TransactionDataSource();
        TransactionRepository transactionRepository = new TransactionRepository(transactionDataSource);
        TransactionService transactionService = new TransactionService(transactionRepository);


        WarehouseServiceImpl warehouseImpl = new WarehouseServiceImpl(
                authService,
                productService,
                transactionService
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