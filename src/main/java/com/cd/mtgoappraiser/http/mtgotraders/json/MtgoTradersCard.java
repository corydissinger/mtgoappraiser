package com.cd.mtgoappraiser.http.mtgotraders.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Cory on 9/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MtgoTradersCard {
    private String name;
    private Double price;
    private String setshort;
    private String verison;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSetshort() {
        return setshort;
    }

    public void setSetshort(String setshort) {
        this.setshort = setshort;
    }

    public String getVerison() {
        return verison;
    }

    public void setVerison(String verison) {
        this.verison = verison;
    }
}
