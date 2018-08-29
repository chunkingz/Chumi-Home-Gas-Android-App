package com.blogspot.chunkingz.eat_it2.Model;

public class Request {
    private String name;
    private String phone;
    private String address;
    private String email;
    private String date;
    private String time;
    private String sizeOfCylinder;
    private String amountPayable;
    private String deliveryMethod;
    private String extraComments;
    private String status;

    public Request() {
    }

    public Request(String name, String phone, String address, String email, String date, String time, String sizeOfCylinder, String amountPayable, String deliveryMethod, String extraComments, String status) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.date = date;
        this.time = time;
        this.sizeOfCylinder = sizeOfCylinder;
        this.amountPayable = amountPayable;
        this.deliveryMethod = deliveryMethod;
        this.extraComments = extraComments;
        this.status = "0";  // 0 = placed, 1 = shipping, 2 = shipped/delivered
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSizeOfCylinder() {
        return sizeOfCylinder;
    }

    public void setSizeOfCylinder(String sizeOfCylinder) {
        this.sizeOfCylinder = sizeOfCylinder;
    }

    public String getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(String amountPayable) {
        this.amountPayable = amountPayable;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getExtraComments() {
        return extraComments;
    }

    public void setExtraComments(String extraComments) {
        this.extraComments = extraComments;
    }
}
