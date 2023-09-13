package com.example.app_apple.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_apple.Model.Product;
import com.example.app_apple.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductDetailFragment extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView quantityTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo các thành phần giao diện
        imageView = findViewById(R.id.ivImageProduct);
        titleTextView = findViewById(R.id.txtTitle);
        priceTextView = findViewById(R.id.txtPrice);
        quantityTextView = findViewById(R.id.txtQuantity);

        // Nhận ID sản phẩm từ Intent
        Intent intent = getIntent();
        int productID = intent.getIntExtra("productId", -1); // -1 là giá trị mặc định nếu không tìm thấy ID

        if (productID != -1) {
            // Lấy sản phẩm từ API bằng ID
            fetchProductDetails(productID);
        } else {
            // Hiển thị thông báo nếu không có ID sản phẩm hợp lệ
            Toast.makeText(this, "ID sản phẩm không hợp lệ.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchProductDetails(int productID) {
        String apiUrl = "https://hi1234-72d066c5f2e0.herokuapp.com/api/products/" + productID;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Xử lý lỗi khi không thể kết nối đến API
                        Toast.makeText(ProductDetailFragment.this, "Không thể kết nối đến API.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Phân tích dữ liệu JSON để lấy thông tin sản phẩm
                                Product product = parseProductDetails(jsonData);
                                if (product != null) {
                                    // Hiển thị thông tin sản phẩm trên giao diện
                                    displayProductDetails(product);
                                } else {
                                    // Hiển thị thông báo nếu không tìm thấy sản phẩm
                                    Toast.makeText(ProductDetailFragment.this, "Không tìm thấy sản phẩm với ID " + productID, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // Xử lý lỗi khi phân tích dữ liệu JSON thất bại
                                Toast.makeText(ProductDetailFragment.this, "Lỗi phân tích dữ liệu JSON.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private Product parseProductDetails(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        int id = json.getInt("id");
        String title = json.getString("title");
        double price = json.getDouble("price");
        String imageUrl = json.getString("images");
        String description = json.getString("description");
        int quantity = json.getInt("quantity");

        return new Product(id, title, price, description, imageUrl);
    }

    private void displayProductDetails(Product product) {
        // Hiển thị thông tin sản phẩm trên giao diện
        Picasso.get().load(product.getImageUrl()).into(imageView);
        titleTextView.setText(product.getTitle());
        priceTextView.setText(String.valueOf((int) product.getPrice()));
        quantityTextView.setText(product.getDescription());
    }
}
