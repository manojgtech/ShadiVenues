package com.ebaba.shadivenues;

public class Venues {
    public String title;
    public String imgurl;
    public String id;
    public String  address;
    public String capacity;


    public Venues(String title, String imgurl, String id, String address, String capacity) {
        this.title = title;
        this.imgurl = imgurl;
        this.id = id;
        this.address = address;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getTitle() {
        return title;
    }

    public Venues(String title, String imgurl) {
        this.title = title;
        this.imgurl = imgurl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
