package com.garden.gardenorder.Model;

/**
 * Created by Nedim on 3/26/2018.
 */

public class Topping {
    String toppingName;

    public Topping() {
    }

    public Topping(String toppingName) {
        this.toppingName = toppingName;

    }

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }
}
