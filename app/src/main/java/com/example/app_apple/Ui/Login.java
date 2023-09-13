package com.example.app_apple.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_apple.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private EditText edtUser;
    private EditText edtPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các thành phần giao diện
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);

        TextView txtSignup = findViewById(R.id.txtSignup);
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng ký (SignUpActivity)
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        // Bắt sự kiện khi người dùng nhấn nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy email và mật khẩu mà người dùng đã nhập
                String email = edtUser.getText().toString();
                String password = edtPass.getText().toString();

                // Kiểm tra thông tin đầu vào (bạn có thể thêm kiểm tra khác)
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tiến hành gọi API để xác thực người dùng
                authenticateUser(email, password);
            }
        });
    }

    // Phương thức xác thực người dùng
    // Phương thức xác thực người dùng
    private void authenticateUser(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        // Chuẩn bị yêu cầu API (thay đổi URL theo API của bạn)
        Request request = new Request.Builder()
                .url("https://raw.githubusercontent.com/vanquyet192/app_android/main/u.json")
                .build();

        // Thực hiện yêu cầu API
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        // Phân tích phản hồi JSON
                        JSONObject responseObject = new JSONObject(responseBody);
                        JSONArray userArray = responseObject.getJSONArray("hydra:member");
                        boolean isAuthenticated = false;

                        // Duyệt qua danh sách người dùng và kiểm tra xem thông tin đăng nhập có khớp với bất kỳ người dùng nào không
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject userObject = userArray.getJSONObject(i);
                            String apiEmail = userObject.getString("email");
                            String apiPassword = userObject.getString("password");

                            if (email.equals(apiEmail) && password.equals(apiPassword)) {
                                isAuthenticated = true;
                                break; // Thoát khỏi vòng lặp nếu xác thực thành công
                            }
                        }

                        if (isAuthenticated) {
                            // Xác thực thành công, chuyển đến màn hình chính (MainActivity)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    // Tạo Intent để khởi động MainActivity
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);

                                    // Kết thúc hoạt động đăng nhập để ngăn không quay lại sau khi đã đăng nhập
                                    finish();
                                }
                            });
                        } else {
                            // Xác thực thất bại (email hoặc mật khẩu không khớp)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, "Email hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
