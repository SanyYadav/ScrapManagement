package com.example.scrapmanagement;

public class NormalUserMember {

    String uid, name, phone, address, url;

    public NormalUserMember(){

    }

    public NormalUserMember(String uid, String name, String phone, String address, String url) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
