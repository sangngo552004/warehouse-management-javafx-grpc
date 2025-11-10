package server.datasource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.model.Product;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String PRODUCTS_FILE = "data/products.json";

    public List<Product> loadProducts() {
        try (Reader reader = new FileReader(PRODUCTS_FILE)) {
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            List<Product> productList = gson.fromJson(reader, productListType);
            System.out.println("ProductDataSource: Đã tải " + productList.size() + " sản phẩm.");
            return productList != null ? productList : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Không tìm thấy file " + PRODUCTS_FILE + ", trả về danh sách rỗng.");
            return new ArrayList<>();
        }
    }

    public void saveProducts(List<Product> products) {
        try (Writer writer = new FileWriter(PRODUCTS_FILE)) {
            gson.toJson(products, writer);
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu file sản phẩm: " + e.getMessage());
        }
    }
}