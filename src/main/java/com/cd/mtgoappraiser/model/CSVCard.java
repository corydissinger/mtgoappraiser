package com.cd.mtgoappraiser.model;

/**
 * Created by Cory on 7/16/2016.
 */
public class CSVCard {
    private String name;
    private Integer quantity;
    private String set;
    private boolean isPremium;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
