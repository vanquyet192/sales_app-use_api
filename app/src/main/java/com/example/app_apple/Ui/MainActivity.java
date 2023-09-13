package com.example.app_apple.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_apple.Adapter.ProductAdapter;
import com.example.app_apple.Fragment.ProductDetailFragment;
import com.example.app_apple.Model.Product;
import com.example.app_apple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    static final String API_URL = "https://raw.githubusercontent.com/vanquyet192/app_android/main/a.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        int spanCount = 2; // Số cột bạn muốn hiển thị trong hàng ngang
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));

        // Khởi tạo danh sách sản phẩm và truyền vào Adapter
        List<Product> productList = new ArrayList<>();


        productAdapter = new ProductAdapter(new ArrayList<>(), new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // Chuyển đến màn hình chi tiết sản phẩm và truyền ID sản phẩm
                Intent intent = new Intent(MainActivity.this, ProductDetailFragment.class);
                intent.putExtra("productId", product.getId());
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(productAdapter);

        ImageButton searchButton = findViewById(R.id.SearchButton);
        EditText searchEditText = findViewById(R.id.SearchEditText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    // Gọi hàm tìm kiếm sản phẩm với từ khóa keyword và truyền productAdapter
                    Search.searchProducts(MainActivity.this, keyword, productAdapter);
                }
            }
        });

        fetchProducts();
    }

    private void fetchProducts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Cập nhật danh sách sản phẩm từ dữ liệu JSON
                                List<Product> products = parseJson(jsonData);
                                productAdapter.setProductList(products);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private List<Product> parseJson(String jsonData) throws JSONException {
        List<Product> productList = new ArrayList<>();
        JSONObject json = new JSONObject(jsonData);
        JSONArray productsArray = json.getJSONArray("products");

        for (int i = 0; i < productsArray.length(); i++) {
            JSONObject productObject = productsArray.getJSONObject(i);
            int id = productObject.getInt("id");
            String title = productObject.getString("title");
            double price = productObject.getDouble("price");
            String description = productObject.getString("description");
            String imageUrl = productObject.getString("images");

            Product product = new Product(id, title, price, description, imageUrl);
            productList.add(product);
        }

        return productList;
    }
}
