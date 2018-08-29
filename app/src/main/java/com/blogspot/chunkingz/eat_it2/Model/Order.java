package com.blogspot.chunkingz.eat_it2.Model;

public class Order {
    private String Date;
    private String Time;
    private String CylinderSize;
    private String AmountPayable;
    private String DeliveryMethod;
    private String ExtraComments;

    public Order() {
    }

    public Order(String date, String time, String cylinderSize, String amountPayable, String deliveryMethod, String extraComments) {
        Date = date;
        Time = time;
        CylinderSize = cylinderSize;
        AmountPayable = amountPayable;
        DeliveryMethod = deliveryMethod;
        ExtraComments = extraComments;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCylinderSize() {
        return CylinderSize;
    }

    public void setCylinderSize(String cylinderSize) {
        CylinderSize = cylinderSize;
    }

    public String getAmountPayable() {
        return AmountPayable;
    }

    public void setAmountPayable(String amountPayable) {
        AmountPayable = amountPayable;
    }

    public String getDeliveryMethod() {
        return DeliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        DeliveryMethod = deliveryMethod;
    }

    public String getExtraComments() {
        return ExtraComments;
    }

    public void setExtraComments(String extraComments) {
        ExtraComments = extraComments;
    }
}
