package server;

import com.group9.warehouse.grpc.*;
import server.model.Product;
import server.model.User;
import io.grpc.stub.StreamObserver;
import server.service.AuthService;
import server.service.HistoryService;
import server.service.ProductService;
import server.model.Transaction;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WarehouseServiceImpl extends WarehouseServiceGrpc.WarehouseServiceImplBase {

    private final AuthService authService;
    private final ProductService productService;
    private final HistoryService historyService;

    public WarehouseServiceImpl(AuthService authService, ProductService productService,
                                HistoryService historyService) {
        this.authService = authService;
        this.productService = productService;
        this.historyService = historyService;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Nhận được yêu cầu đăng nhập cho: " + request.getUsername());

        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> userOptional = authService.validateUser(username, password);

        LoginResponse.Builder responseBuilder = LoginResponse.newBuilder();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            responseBuilder
                    .setSuccess(true)
                    .setMessage("Đăng nhập thành công!")
                    .setRole(user.getRole());
            System.out.println("Đăng nhập thành công cho: " + username);
        } else {
            responseBuilder
                    .setSuccess(false)
                    .setMessage("Sai tên đăng nhập hoặc mật khẩu.");
            System.out.println("Đăng nhập thất bại cho: " + username);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getProducts(EmptyRequest request, StreamObserver<ProductListResponse> responseObserver) {
        ProductListResponse.Builder response = ProductListResponse.newBuilder();

        List<Product> modelProducts = productService.getAllProducts();

        for (Product modelProduct : modelProducts) {
            com.group9.warehouse.grpc.Product grpcProduct =
                    com.group9.warehouse.grpc.Product.newBuilder()
                            .setProductId(modelProduct.getProductId())
                            .setProductName(modelProduct.getProductName()) //
                            .build();
            response.addProducts(grpcProduct);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addProduct(AddProductRequest request, StreamObserver<ServiceResponse> responseObserver) {
        boolean success = productService.addProduct(request.getProductId(), request.getProductName()); //

        ServiceResponse.Builder response = ServiceResponse.newBuilder();
        if (success) {
            response.setSuccess(true).setMessage("Thêm sản phẩm thành công."); //
        } else {
            response.setSuccess(false).setMessage("Thêm thất bại. ID sản phẩm có thể đã tồn tại."); //
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getInventory(EmptyRequest request, StreamObserver<InventoryResponse> responseObserver) {
        InventoryResponse.Builder response = InventoryResponse.newBuilder();
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            InventoryItem item =
                    InventoryItem.newBuilder()
                            .setProductName(product.getProductName())
                            .setQuantity(product.getQuantity()) //
                            .build();
            response.addItems(item);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void importProduct(TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {

        System.out.print( "WarehouseServiceImpl.importProduct: " + request.getProductId() + " " + request.getQuantity() + "\n" );

        ProductService.TransactionResponse result = productService.importProduct(
                request.getProductId(),
                request.getQuantity()
        );

        String productName = productService.getProductById(request.getProductId())
                .map(server.model.Product::getProductName)
                .orElse(request.getProductId());

        // Xác định kết quả
        String logResult = result.success ? "Success" : "Failed (" + result.message + ")";

        // GỌI HISTORY SERVICE VỚI THÔNG TIN TỪ CLIENT
        historyService.logTransaction(
                request.getClientName(), // <-- Đây là username client gửi lên
                "IMPORT",
                productName,
                request.getQuantity(),
                logResult
        );

        TransactionResponse response = TransactionResponse.newBuilder()
                .setSuccess(result.success)
                .setMessage(result.message)
                .setNewQuantity(result.newQuantity)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exportProduct(TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        ProductService.TransactionResponse result = productService.exportProduct(
                request.getProductId(),
                request.getQuantity()
        );
        String productName = productService.getProductById(request.getProductId())
                .map(server.model.Product::getProductName)
                .orElse(request.getProductId());

        String logResult = result.success ? "Success" : "Failed (" + result.message + ")";

        historyService.logTransaction(
                request.getClientName(),
                "EXPORT",
                productName,
                request.getQuantity(),
                logResult
        );

        TransactionResponse response = TransactionResponse.newBuilder()
                .setSuccess(result.success)
                .setMessage(result.message)
                .setNewQuantity(result.newQuantity)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getHistory(EmptyRequest request, StreamObserver<HistoryResponse> responseObserver) {
        HistoryResponse.Builder response = HistoryResponse.newBuilder();
        List<Transaction> history = historyService.getHistory();

        for (Transaction modelTrans : history) {
            com.group9.warehouse.grpc.Transaction grpcTrans =
                    com.group9.warehouse.grpc.Transaction.newBuilder()
                            .setTimestamp(modelTrans.getTimestamp())
                            .setClientName(modelTrans.getClientName())
                            .setAction(modelTrans.getAction())
                            .setProduct(modelTrans.getProduct()) //
                            .setQuantity(modelTrans.getQuantity())
                            .setResult(modelTrans.getResult())
                            .build();
            response.addTransactions(grpcTrans);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}