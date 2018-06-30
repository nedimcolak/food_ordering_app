package com.garden.gardenorder.Model;

import java.util.List;

/**
 * Ovo je za pop up gdje pita za adresu
 */

public class Request {
    private String phone;
    private String name;
    private String total;
    private String status;
    private String comment;
    private List<Order> food;

    public Request() {
    }

    public Request(String phone, String name, String total, String status, String comment, List<Order> food) {
        this.phone = phone;
        this.name = name;
        this.total = total;
        this.food = food;
        this.status = "0"; // 0 poslana narudzba, 1 potvrdjena narudzba, 2 dostavljena narudzba
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFood() {
        return food;
    }

    public void setFood(List<Order> food) {
        this.food = food;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}



