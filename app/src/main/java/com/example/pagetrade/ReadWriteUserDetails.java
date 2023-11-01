package com.example.pagetrade;

public class ReadWriteUserDetails {
    public String mobile,address;

    public ReadWriteUserDetails(String mobile, String address) {
        this.mobile = mobile;
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
