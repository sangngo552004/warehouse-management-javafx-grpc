package server;

import com.group9.warehouse.grpc.LoginRequest;
import com.group9.warehouse.grpc.LoginResponse;
import com.group9.warehouse.grpc.WarehouseServiceGrpc;
import common.model.User;
import io.grpc.stub.StreamObserver;

import java.util.Optional;

public class WarehouseServiceImpl extends WarehouseServiceGrpc.WarehouseServiceImplBase {

    private final DataService dataService;

    public WarehouseServiceImpl(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        System.out.println("Nhận được yêu cầu đăng nhập cho: " + request.getUsername());

        String username = request.getUsername();
        String password = request.getPassword();

        Optional<User> userOptional = dataService.validateUser(username, password);

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
}