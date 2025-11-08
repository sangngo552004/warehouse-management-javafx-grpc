package server.model;

public class Product {
    private String productId;
    private String productName;
    private int quantity;

    public Product() {}
    public Product(String productId, String productName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
