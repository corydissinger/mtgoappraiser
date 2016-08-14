package com.cd.mtgoappraiser.model;

/**
 * Created by Cory on 8/12/2016.
 */
public class AppraisedCard extends MtgGoldfishCard {
    private Double sumPrice;

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }
}
