package com.kads.android.parkingtracker.Model;

import java.io.Serializable;

public class Receipt implements Serializable {
    private String carColor;
    private String carCompany;
    private String carNo;
    private String dateTime;
    private String email;
    private String lotNo;
    private String noOfHours;

    private String priceHour;
    private String paymentAmount;
    private String paymentMethod;
    private String spotNo;
    private String isPaid;

    public Receipt() {
    }

    public Receipt(String carColor, String carCompany, String carNo, String dateTime, String email, String lotNo, String noOfHours, String priceHour, String paymentAmount, String paymentMethod, String spotNo, String isPaid) {
        this.carColor = carColor;
        this.carCompany = carCompany;
        this.carNo = carNo;
        this.dateTime = dateTime;
        this.email = email;
        this.lotNo = lotNo;
        this.noOfHours = noOfHours;
        this.priceHour = priceHour;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.spotNo = spotNo;
        this.isPaid = isPaid;
    }


    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarCompany() {
        return carCompany;
    }

    public void setCarCompany(String carCompany) {
        this.carCompany = carCompany;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getNoOfHours() {
        return noOfHours;
    }

    public void setNoOfHours(String noOfHours) {
        this.noOfHours = noOfHours;
    }

    public String getPriceHour() {
        return priceHour;
    }

    public void setPriceHour(String priceHour) {
        this.priceHour = priceHour;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSpotNo() {
        return spotNo;
    }

    public void setSpotNo(String spotNo) {
        this.spotNo = spotNo;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }
}
