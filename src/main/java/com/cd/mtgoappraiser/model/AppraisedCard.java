package com.cd.mtgoappraiser.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Comparator;

/**
 * Created by Cory on 8/12/2016.
 */
public class AppraisedCard extends MarketCard {
    private Double sumPrice;

    public AppraisedCard(MarketCard aCard) {
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

    public static class AppraisedCardComparator implements Comparator<AppraisedCard> {

        @Override
        public int compare(AppraisedCard left, AppraisedCard right) {
            if(left.getRetailPrice() > right.getRetailPrice()) {
                return -1;
            } else if (left.getRetailPrice() < right.getRetailPrice()) {
                return 1;
            } else {
                return left.getName().compareToIgnoreCase(right.getName());
            }
        }
    }
}
