package server.repository;

import server.datasource.ProductDataSource;
import server.model.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProductRepository {

    private final Map<String, Product> productDatabase;
    private final ProductDataSource dataSource;

    public ProductRepository(ProductDataSource dataSource) {
        this.dataSource = dataSource;
        this.productDatabase = new ConcurrentHashMap<>();
        List<Product> products = dataSource.loadProducts();
        products.forEach(p -> productDatabase.put(p.getProductId(), p));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productDatabase.values());
    }

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(productDatabase.get(productId));
    }

    public synchronized boolean addProduct(Product product) {
        if (productDatabase.containsKey(product.getProductId())) {
            return false;
        }
        productDatabase.put(product.getProductId(), product);
        dataSource.saveProducts(findAll());
        return true;
    }

    public synchronized void updateProduct(Product product) {
        productDatabase.put(product.getProductId(), product);
        dataSource.saveProducts(findAll());
    }
}
