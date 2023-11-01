package com.example.pagetrade;

public class User {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String pass,profileImg;
    private String userType,uid;

    public User() {}
    public User(String name, String email, String phone, String address,String pass, String userType, String profileImg, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profileImg = profileImg;
        this.pass = pass;
        this.userType = userType;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImg() {
        return profileImg;
    }
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
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

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
