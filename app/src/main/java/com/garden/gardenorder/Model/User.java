package com.garden.gardenorder.Model;

/**
 * Created by Faggot on 10/26/2017.
 */

public class User {
    private String Name;
    private String Phone;
    private String IsStaff;


    public User() {

    }

    public User(String name, String phone) {
        Name = name;
        Phone = phone;
        IsStaff = "false";


    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
