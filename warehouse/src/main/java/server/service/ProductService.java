package server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.model.Product; // <-- SỬ DỤNG MODEL CỦA SERVER

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String PRODUCTS_FILE = "data/products.json";
    private final Map<String, Product> productDatabase; // Key là ProductID

    public ProductService() {
        this.productDatabase = loadProducts();
    }

    private Map<String, Product> loadProducts() {
        try (Reader reader = new FileReader(PRODUCTS_FILE)) {
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            List<Product> productList = gson.fromJson(reader, productListType);
            System.out.println("ProductService: Đã tải " + productList.size() + " sản phẩm.");
            return productList.stream()
                    .collect(Collectors.toConcurrentMap(Product::getProductId, product -> product));
        } catch (Exception e) {
            System.err.println("Lỗi: Không tìm thấy file " + PRODUCTS_FILE);
            return new ConcurrentHashMap<>();
        }
    }

    private synchronized void saveProducts() {
        try (Writer writer = new FileWriter(PRODUCTS_FILE)) {
            gson.toJson(new ArrayList<>(productDatabase.values()), writer);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file sản phẩm: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }

    public Optional<Product> getProductById(String productId) {
        return Optional.ofNullable(productDatabase.get(productId));
    }

    public synchronized boolean addProduct(String productId, String productName) {
        if (productDatabase.containsKey(productId)) {
            return false; // Đã tồn tại
        }
        // Tạo POJO mới
        Product newProduct = new Product(productId, productName, 0);
        productDatabase.put(productId, newProduct);
        saveProducts();
        return true;
    }


    public synchronized TransactionResponse importProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return new TransactionResponse(false, "Số lượng phải lớn hơn 0", -1);
        }

        Product product = productDatabase.get(productId);
        if (product == null) {
            return new TransactionResponse(false, "Sản phẩm không tồn tại", -1);
        }

        product.setQuantity(product.getQuantity() + quantity);
        saveProducts(); // Lưu lại file
        return new TransactionResponse(true, "Nhập hàng thành công", product.getQuantity());
    }

    public synchronized TransactionResponse exportProduct(String productId, int quantity) {
        if (quantity <= 0) {
            return new TransactionResponse(false, "Số lượng phải lớn hơn 0", -1);
        }

        Product product = productDatabase.get(productId);
        if (product == null) {
            return new TransactionResponse(false, "Sản phẩm không tồn tại", -1);
        }

        if (product.getQuantity() < quantity) {
            return new TransactionResponse(false, "Không đủ hàng tồn kho", product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - quantity);
        saveProducts(); // Lưu lại file
        return new TransactionResponse(true, "Xuất hàng thành công", product.getQuantity());
    }

    // Lớp nội bộ tiện ích
    public static class TransactionResponse {
        public final boolean success;
        public final String message;
        public final int newQuantity;

        public TransactionResponse(boolean success, String message, int newQuantity) {
            this.success = success;
            this.message = message;
            this.newQuantity = newQuantity;
        }
    }

}