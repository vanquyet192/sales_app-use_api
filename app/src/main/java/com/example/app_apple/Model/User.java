package com.example.app_apple.Model;

public class User {
    private String email;
    private String password;
    private String address;
    private Integer phone;
    private String fullname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public User(String email, String password, String address, String phone, String fullname) {
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = Integer.valueOf(phone);
        this.fullname = fullname;
    }
}
