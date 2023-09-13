package com.example.app_apple.Api;

import com.example.app_apple.Model.Product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    @GET(value = "products/{id}")
    Call<Product> getProductById(@Path("id") int id);
}
