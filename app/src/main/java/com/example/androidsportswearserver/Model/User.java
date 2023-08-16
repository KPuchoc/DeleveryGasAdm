package com.example.androidsportswearserver.Model;

import androidx.annotation.NonNull;

public class User {
    private String Name,Password,Phone,IsStaff;

    public User(String name, String password) {
        this.Name = name;
        Password = password;
    }

    public User() {
    }

    public  String getName() {

        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
