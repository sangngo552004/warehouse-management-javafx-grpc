package server.service;

import server.model.Product;
import server.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    public boolean addProduct(String productId, String name) {
        Product product = new Product(productId, name, 0);
        return productRepository.addProduct(product);
    }

    public TransactionResponse importProduct(String productId, int quantity) {
        if (quantity <= 0) return new TransactionResponse(false, "Số lượng phải lớn hơn 0", -1);

        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) return new TransactionResponse(false, "Sản phẩm không tồn tại", -1);

        Product p = opt.get();
        p.setQuantity(p.getQuantity() + quantity);
        productRepository.updateProduct(p);
        return new TransactionResponse(true, "Nhập hàng thành công", p.getQuantity());
    }

    public TransactionResponse exportProduct(String productId, int quantity) {
        if (quantity <= 0) return new TransactionResponse(false, "Số lượng phải lớn hơn 0", -1);

        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isEmpty()) return new TransactionResponse(false, "Sản phẩm không tồn tại", -1);

        Product p = opt.get();
        if (p.getQuantity() < quantity) return new TransactionResponse(false, "Không đủ hàng tồn kho", p.getQuantity());

        p.setQuantity(p.getQuantity() - quantity);
        productRepository.updateProduct(p);
        return new TransactionResponse(true, "Xuất hàng thành công", p.getQuantity());
    }

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
