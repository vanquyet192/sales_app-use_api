package com.example.app_apple.Ui;

import android.app.Activity;

import com.example.app_apple.Adapter.ProductAdapter;
import com.example.app_apple.Model.Product;

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

public class Search {

    public static void searchProducts(Activity activity, String keyword, ProductAdapter productAdapter) {
        OkHttpClient client = new OkHttpClient();
        String searchUrl = MainActivity.API_URL + "?q=" + keyword;

        Request request = new Request.Builder()
                .url(searchUrl)
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
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

    private static List<Product> parseJson(String jsonData) throws JSONException {
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
