package com.example.scrapmanagement;

public class Postmember {

    String name, phone, address, desc, postUri, time, type; //currentuid;

    public Postmember() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //public String getCurrentuid() {
    //    return currentuid;
    //}

    //public void setCurrentuid(String currentuid) {
    //   this.currentuid = currentuid;
    // }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
