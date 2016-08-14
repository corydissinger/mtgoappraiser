package com.cd.mtgoappraiser.model;

/**
 * Created by Cory on 8/10/2016.
 */
public class MtgGoldfishCard extends Card {
    private Double retailPrice;
    private Double buyPrice;

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }
}
