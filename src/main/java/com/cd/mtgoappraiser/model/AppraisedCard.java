package com.cd.mtgoappraiser.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Cory on 8/12/2016.
 */
public class AppraisedCard extends MtgGoldfishCard {
    private Double sumPrice;

    public AppraisedCard(MtgGoldfishCard aCard) {
        super(aCard);
    }

    public AppraisedCard(AppraisedCard aCard) {
        super(aCard);
        this.setSumPrice(aCard.getSumPrice());
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public boolean equals(AppraisedCard right) {
        return super.equals(right);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
