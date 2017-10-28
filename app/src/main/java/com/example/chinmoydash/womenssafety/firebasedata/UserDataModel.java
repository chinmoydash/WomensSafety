package com.example.chinmoydash.womenssafety.firebasedata;

/**
 * Created by chinmoydash on 24/10/17.
 */

public class UserDataModel {

    String name;
    String email;
    String mobile;

    public UserDataModel() {
    }

    public UserDataModel(String name, String email, String mobile) {

        this.name = name;
        this.email = email;
        this.mobile = mobile;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}