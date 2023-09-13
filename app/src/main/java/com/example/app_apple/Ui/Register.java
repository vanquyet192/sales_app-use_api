package com.example.app_apple.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_apple.Api.ApiService;
import com.example.app_apple.Model.User;
import com.example.app_apple.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private EditText edtFullName;
    private EditText edtRegEmail;
    private EditText edtRegPass;
    private EditText edtAddress;
    private EditText edtPhone;
    private TextView txtLogin;
    // Lấy tham chiếu đến TextView txtLogin


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ các thành phần giao diện

        edtFullName = findViewById(R.id.edtFullName);
        edtRegEmail = findViewById(R.id.edtRegEmail);
        edtRegPass = findViewById(R.id.edtRegPass);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        txtLogin = findViewById(R.id.txtLogin);

        TextView txtSignup = findViewById(R.id.txtLogin);
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng ký (SignUpActivity)
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi người dùng nhấn nút Đăng ký
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập liệu
                String fullName = edtFullName.getText().toString();
                String email = edtRegEmail.getText().toString();
                String password = edtRegPass.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhone.getText().toString();
                // Xử lý khi không nhập đầy đủ thông tin
                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Dừng xử lý nếu có trường trống
                }
                // Tạo một đối tượng User
                User user = new User(email, password, address, phone, fullName);

                // Gửi yêu cầu đăng ký qua API
                sendRegistrationRequest(user);

            }
        });
    }

    private void sendRegistrationRequest(User user) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hi1234-72d066c5f2e0.herokuapp.com/").addConverterFactory(GsonConverterFactory.create()).build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<Void> call = apiService.registerUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Đăng ký thành công, xử lý tại đây
                    Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    // Tạo Intent để quay lại trang đăng nhập
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);

                    // Kết thúc hoạt động đăng ký để ngăn quay lại sau khi đã đăng ký
                    finish();
                } else {
                    // Đăng ký thất bại, xử lý tại đây
                    Toast.makeText(Register.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi gửi yêu cầu
                Toast.makeText(Register.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
